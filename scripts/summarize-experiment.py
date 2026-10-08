"""Consolida evidências existentes sem executar ou modificar o conversor."""
import csv
import hashlib
import json
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parent.parent
OUT = ROOT / "resultados/etapa5"
data = json.loads((OUT / "experiment.json").read_text(encoding="utf-8"))
assert data["restored_byte_for_byte"]
manifest = json.loads((ROOT / "resultados/etapa4/manifest.json").read_text(encoding="utf-8"))
for path, digest in manifest["source_sha256"].items():
    archived = ROOT / "resultados/etapa5/baseline/RomanNumerals.java" if path == "src/RomanNumerals.java" else ROOT / path
    assert hashlib.sha256(archived.read_bytes()).hexdigest() == digest, path


def write(name, rows):
    with (OUT / name).open("w", encoding="utf-8", newline="") as stream:
        writer = csv.DictWriter(stream, fieldnames=list(rows[0]))
        writer.writeheader()
        writer.writerows(rows)


groups, properties, failures = [], [], []
header = re.compile(r"timestamp = .*?, (Seed\d+Properties):(CT-PBT-\d+) seed=(\d+) =")
for run in data["runs"]:
    groups.append({k: run[k] for k in ("variant", "group", "tests", "failures", "exit_code")})
    for case in run["cases"]:
        if case["status"] == "failure":
            failures.append({"variant": run["variant"], "group": run["group"],
                             "class": case["class"], "test": case["name"], "message": case["message"]})
    if run["group"] == "examples":
        continue
    log = (OUT / run["variant"] / run["group"] / "run.log").read_text(encoding="utf-8")
    matches = list(header.finditer(log))
    assert len(matches) == 30, (run["variant"], run["group"], len(matches))
    for index, match in enumerate(matches):
        block = log[match.end():matches[index + 1].start() if index + 1 < len(matches) else len(log)]
        cls, prop, seed = match.groups()
        case = next(c for c in run["cases"] if c["class"] == cls and c["name"] == f"{prop} seed={seed}")
        tries = int(re.search(r"^tries = (\d+)", block, re.M)[1])
        checks = int(re.search(r"^checks = (\d+)", block, re.M)[1])
        samples = dict(re.findall(r"^(Shrunk Sample(?: \(\d+ steps\))?|Original Sample|Sample)\n-+\n  ([^\n]+)", block, re.M))
        shrunk = next((v for k, v in samples.items() if k.startswith("Shrunk")), "")
        steps = re.search(r"^Shrunk Sample \((\d+) steps\)", block, re.M)
        properties.append({"variant": run["variant"], "group": run["group"], "property": prop,
                           "seed": seed, "status": case["status"], "tries": tries, "checks": checks,
                           "discarded": tries - checks, "original_sample": samples.get("Original Sample", ""),
                           "final_sample": shrunk or samples.get("Sample", ""),
                           "shrink_steps": steps[1] if steps else ""})
        assert checks > 0 and tries == checks
        if case["status"] == "passed":
            assert checks == 1000

write("groups.csv", groups)
write("property-runs.csv", properties)
write("counterexamples.csv", failures)
matrix = []
for variant in data["variant_sha256"]:
    row = {"variant": variant}
    for p in range(1, 7):
        subset = [r for r in properties if r["variant"] == variant and r["group"] == "properties" and r["property"] == f"CT-PBT-{p:02}"]
        row[f"CT-PBT-{p:02}"] = sum(r["status"] == "failure" for r in subset)
    matrix.append(row)
write("detection.csv", matrix)
for variant in data["variant_sha256"]:
    rows = [r for r in properties if r["variant"] == variant and r["group"] == "properties"]
    print(variant, "checks primários:", sum(r["checks"] for r in rows),
          "P01 semente 42:", next(r for r in rows if r["seed"] == "42" and r["property"] == "CT-PBT-01"))
assert data["runs"][-1]["tests"] == 134 and data["runs"][-1]["failures"] == 0
print("CSV gerados; hashes da etapa 4 e restauração conferidos.")
