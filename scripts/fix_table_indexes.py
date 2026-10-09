import glob
import re

for path in glob.glob('backend/src/main/java/com/vehiclerental/entity/*.java'):
    with open(path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    new_content = re.sub(r'@Table\(\s*name\s*=\s*"([^"]+)"\s*,\s*indexes\s*=\s*\{[^\}]+\}\s*\)', r'@Table(name = "\1")', content)
    new_content = re.sub(r'@Table\(\s*name\s*=\s*"([^"]+)"\s*,\s*uniqueConstraints\s*=\s*\{[^\}]+\}\s*,\s*indexes\s*=\s*\{[^\}]+\}\s*\)', r'@Table(name = "\1")', new_content)
    
    if new_content != content:
        with open(path, 'w', encoding='utf-8') as f:
            f.write(new_content)
        print("Updated table annotation in", path)
