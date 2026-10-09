import os
import re

def capitalize(s):
    return s[0].upper() + s[1:] if s else s

def process_pojo_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        lines = f.readlines()

    content = "".join(lines)
    class_match = re.search(r'public\s+(?:class)\s+(\w+)(?:\s+extends\s+\w+)?(?:\s+implements\s+[^{]+)?\s*\{', content)
    if not class_match:
        return
    class_name = class_match.group(1)

    # Check if this class had @Data, @Getter, @Setter, @Builder
    had_lombok = any(ann in content for ann in ['@Data', '@Getter', '@Setter', '@Builder', '@NoArgsConstructor', '@AllArgsConstructor'])
    if not had_lombok:
        return

    # Clean lombok annotations from class header
    for ann in ['@Data', '@Getter', '@Setter', '@Builder', '@NoArgsConstructor', '@AllArgsConstructor']:
        content = re.sub(r'^[ \t]*' + re.escape(ann) + r'\s*\n', '', content, flags=re.MULTILINE)

    # Clean @Builder.Default on fields
    content = re.sub(r'^[ \t]*@Builder\.Default\s*\n', '', content, flags=re.MULTILINE)

    # Parse instance fields inside the class (excluding nested static classes, methods, or existing constructors)
    # We find class body start
    match_start = re.search(r'public\s+(?:class)\s+' + class_name + r'(?:\s+extends\s+\w+)?(?:\s+implements\s+[^{]+)?\s*\{', content)
    start_pos = match_start.end()
    
    # Extract fields
    # Look for: private [final]? <type> <name> [= <init>]?;
    field_pattern = re.compile(r'^\s*private\s+(?!static|final\s+org\.slf4j)(?:final\s+)?([A-Za-z0-9_<>,\.\? ]+?)\s+(\w+)\s*(?:=\s*[^;]+)?\s*;', re.MULTILINE)
    
    fields = []
    for m in field_pattern.finditer(content[start_pos:]):
        ftype = m.group(1).strip()
        fname = m.group(2).strip()
        fields.append((ftype, fname))

    if not fields:
        return

    # Check if getters/setters or builder already exist
    if f"public {class_name}()" in content:
        # already has constructors/getters
        pass
    else:
        # Build No-Arg Constructor
        no_arg = f"\n    public {class_name}() {{\n    }}\n"

        # Build All-Arg Constructor
        all_params = ", ".join([f"{ftype} {fname}" for ftype, fname in fields])
        all_assign = "\n".join([f"        this.{fname} = {fname};" for _, fname in fields])
        all_arg = f"\n    public {class_name}({all_params}) {{\n{all_assign}\n    }}\n"

        # Build Getters and Setters
        accessors = []
        for ftype, fname in fields:
            getter_name = f"get{capitalize(fname)}"
            if ftype == "boolean" or ftype == "Boolean":
                # provide both getX and isX if needed, or standard getX
                pass
            setter_name = f"set{capitalize(fname)}"
            
            acc = f"""
    public {ftype} {getter_name}() {{
        return this.{fname};
    }}

    public void {setter_name}({ftype} {fname}) {{
        this.{fname} = {fname};
    }}"""
            # If boolean, also add isX if starts with is or standard
            if (ftype == "boolean" or ftype == "Boolean") and not fname.startswith("is"):
                acc += f"""
    public {ftype} is{capitalize(fname)}() {{
        return this.{fname};
    }}"""
            elif fname.startswith("is"):
                # e.g. isActive -> getActive and getIsActive
                without_is = fname[2:]
                acc += f"""
    public {ftype} get{without_is}() {{
        return this.{fname};
    }}
    public void set{without_is}({ftype} {fname}) {{
        this.{fname} = {fname};
    }}"""
            accessors.append(acc)

        # Build Builder Class
        builder_fields_decl = "\n".join([f"        private {ftype} {fname};" for ftype, fname in fields])
        builder_methods = []
        for ftype, fname in fields:
            b_method = f"""        public Builder {fname}({ftype} {fname}) {{
            this.{fname} = {fname};
            return this;
        }}"""
            builder_methods.append(b_method)

        builder_build_assign = ", ".join([f"this.{fname}" for _, fname in fields])
        builder_class = f"""
    public static Builder builder() {{
        return new Builder();
    }}

    public static class Builder {{
{builder_fields_decl}

{chr(10).join(builder_methods)}

        public {class_name} build() {{
            return new {class_name}({builder_build_assign});
        }}
    }}
"""

        # Insert before the last closing brace
        last_brace = content.rfind('}')
        code_to_insert = no_arg + all_arg + "\n".join(accessors) + "\n" + builder_class
        content = content[:last_brace] + code_to_insert + content[last_brace:]

    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)
    print(f"Generated POJO: {filepath}")

TARGET_DIRS = [
    r"d:\Projects\Vehcile_Rental_System\backend\src\main\java\com\vehiclerental\entity",
    r"d:\Projects\Vehcile_Rental_System\backend\src\main\java\com\vehiclerental\dto\request",
    r"d:\Projects\Vehcile_Rental_System\backend\src\main\java\com\vehiclerental\dto\response"
]

for tdir in TARGET_DIRS:
    for root, dirs, files in os.walk(tdir):
        for f in files:
            if f.endswith('.java'):
                process_pojo_file(os.path.join(root, f))
