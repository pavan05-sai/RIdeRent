import os
import re

JAVA_ROOT = r"d:\Projects\Vehcile_Rental_System\backend\src\main\java"

def capitalize(s):
    return s[0].upper() + s[1:] if s else s

def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    original = content
    class_match = re.search(r'public\s+(?:class|enum)\s+(\w+)', content)
    if not class_match:
        return
    class_name = class_match.group(1)

    # 1. Handle @Slf4j
    if '@Slf4j' in content:
        content = re.sub(r'@Slf4j\s*\n', '', content)
        content = re.sub(r'import lombok\.extern\.slf4j\.Slf4j;\s*\n', '', content)
        # Add logger field right after class declaration
        logger_decl = f"\n    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger({class_name}.class);\n"
        content = re.sub(r'(public\s+class\s+' + class_name + r'[^{]*\{)', r'\1' + logger_decl, content, count=1)

    # 2. Handle @RequiredArgsConstructor
    if '@RequiredArgsConstructor' in content:
        content = re.sub(r'@RequiredArgsConstructor\s*\n', '', content)
        content = re.sub(r'import lombok\.RequiredArgsConstructor;\s*\n', '', content)
        
        # Find all private final fields
        final_fields = re.findall(r'private\s+final\s+([A-Za-z0-9_<>, ?]+?)\s+(\w+)\s*;', content)
        if final_fields:
            params = ", ".join([f"{ftype.strip()} {fname}" for ftype, fname in final_fields])
            assigns = "\n".join([f"        this.{fname} = {fname};" for _, fname in final_fields])
            ctor = f"\n    public {class_name}({params}) {{\n{assigns}\n    }}\n"
            
            # Insert constructor before the last closing brace
            last_brace_idx = content.rfind('}')
            content = content[:last_brace_idx] + ctor + content[last_brace_idx:]

    # Remove standard lombok imports
    content = re.sub(r'import lombok\.[^;]+;\s*\n', '', content)

    if content != original:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Updated: {filepath}")

for root, dirs, files in os.walk(JAVA_ROOT):
    for f in files:
        if f.endswith('.java'):
            process_file(os.path.join(root, f))
