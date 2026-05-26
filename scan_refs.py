from pathlib import Path
import re
root = Path('src/main/resources/static')
allowed = {p.name for p in root.glob('*.html')}
pattern = re.compile(r'"([^"\']+\.html)"|\'([^"\']+\.html)\'')
refs = {}
for path in root.rglob('*.html'):
    text = path.read_text(encoding='utf-8', errors='replace')
    for m in pattern.finditer(text):
        ref = m.group(1) or m.group(2)
        if ref and ref not in allowed:
            refs.setdefault(path.name, []).append(ref)
for path in sorted(refs):
    print(path)
    for item in sorted(set(refs[path])):
        print('  ', item)
