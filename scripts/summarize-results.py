"""Extrai evidências dos logs de etapa 4 e registra hashes do código avaliado.

Uso: python scripts/summarize-results.py
Requer somente a biblioteca padrão do Python 3.
"""
from pathlib import Path
import csv
import hashlib
import json
import re

ROOT = Path(__file__).resolve().parent.parent
EVIDENCE = ROOT / "resultados" / "etapa4"
historical = json.loads((EVIDENCE / "manifest.json").read_text(encoding="utf-8"))
for original_path, original_digest in historical["source_sha256"].items():
    if hashlib.sha256((ROOT / original_path).read_bytes()).hexdigest() != original_digest:
        raise RuntimeError("Código difere da campanha original. Use a revisão 78399cf em uma cópia separada; não sobrescrever a etapa 4.")


def parse_group(group):
    text = (EVIDENCE / f"{group}.log").read_text(encoding="utf-8")
    totals = re.findall(r"Tests run: (\d+), Failures: (\d+), Errors: (\d+), Skipped: (\d+)", text)
    if not totals or "BUILD SUCCESS" not in text:
        raise ValueError(f"Execução sem sucesso ou sem totais: {group}")
    tests, failures, errors, skipped = map(int, totals[-1])
    completed = re.search(r"Finished at: ([^\r\n]+)", text)
    duration = re.search(r"Total time:\s+([^\r\n]+)", text)
    return {
        "group": group, "tests": tests, "failures": failures,
        "errors": errors, "skipped": skipped,
        "finished_at": completed.group(1).strip(),
        "maven_total_time": duration.group(1).strip(),
    }


def property_runs(text):
    runs = []
    pattern = r"timestamp = [^\r\n]+, Seed(\d+)Properties:(CT-PBT-\d+) seed=\d+ =\s*(.*?)(?=timestamp =|\[INFO\]|\Z)"
    for match in re.finditer(pattern, text, re.S):
        seed, case, report = match.groups()
        tries = int(re.search(r"^tries = (\d+)", report, re.M).group(1))
        checks = int(re.search(r"^checks = (\d+)", report, re.M).group(1))
        mode = re.search(r"^generation = (\w+)", report, re.M).group(1)
        reported_seed = re.search(r"^seed = (\d+)", report, re.M).group(1)
        if seed != reported_seed:
            raise ValueError("Semente do rótulo difere da semente efetiva")
        runs.append({"seed": seed, "test_id": case, "tries": tries,
                     "checks": checks, "discarded": tries - checks, "mode": mode})
    expected = {(str(seed), f"CT-PBT-{number:02}")
                for seed in (42, 2024, 2026, 3999, 104729) for number in range(1, 7)}
    if {(r["seed"], r["test_id"]) for r in runs} != expected or len(runs) != 30:
        raise ValueError("Campanha não contém exatamente seis propriedades por semente")
    if any(r["mode"] != "RANDOMIZED" or r["tries"] != 1000 or r["checks"] != 1000 for r in runs):
        raise ValueError("Campanha não completou a configuração planejada")
    return runs


def distributions(text):
    rows = []
    pattern = r"\[Seed(\d+)Properties:(CT-PBT-\d+) seed=\d+\] \(1000\) ([\w-]+) =\s*(.*?)(?=timestamp =|\[INFO\]|\Z)"
    for match in re.finditer(pattern, text, re.S):
        seed, case, statistic, report = match.groups()
        counts = re.findall(r"^\s*(\S+)\s+\(\s*(\d+)\)\s*:", report, re.M)
        if sum(int(count) for _, count in counts) != 1000:
            raise ValueError(f"Distribuição incompleta: {seed}, {case}, {statistic}")
        for category, count in counts:
            rows.append({"seed": seed, "test_id": case, "statistic": statistic,
                         "category": category, "count": int(count)})
    if len({(r["seed"], r["test_id"], r["statistic"]) for r in rows}) != 90:
        raise ValueError("Esperadas três estatísticas para cada uma das 30 execuções")
    return rows


def write_csv(name, rows):
    with (EVIDENCE / name).open("w", encoding="utf-8", newline="") as stream:
        writer = csv.DictWriter(stream, fieldnames=list(rows[0]))
        writer.writeheader()
        writer.writerows(rows)


groups = [parse_group(g) for g in ("infrastructure", "examples", "properties", "exhaustive", "all")]
expected_tests = {"infrastructure": 35, "examples": 69, "properties": 30, "exhaustive": 2, "all": 134}
assert all(g["tests"] == expected_tests[g["group"]] and
           g["failures"] == g["errors"] == g["skipped"] == 0 for g in groups)
property_log = (EVIDENCE / "properties.log").read_text(encoding="utf-8")
runs = property_runs(property_log)
stats = distributions(property_log)
# Também conferir que a suíte integrada executou a mesma campanha.
integrated = property_runs((EVIDENCE / "all.log").read_text(encoding="utf-8"))
assert runs == integrated
write_csv("groups.csv", groups)
write_csv("properties.csv", runs)
write_csv("distributions.csv", stats)
files = sorted([ROOT / "pom.xml", *ROOT.glob("src/*.java"), *ROOT.glob("tests/*.java"),
                ROOT / "tests/resources/junit-platform.properties"])
manifest = {
    "baseline": "etapa4-sem-defeitos-controlados",
    "source_sha256": {str(p.relative_to(ROOT)).replace("\\", "/"): hashlib.sha256(p.read_bytes()).hexdigest()
                      for p in files},
    "group_runs": groups,
    "primary_property_evaluations": sum(r["checks"] for r in runs),
    "discarded": sum(r["discarded"] for r in runs),
    "integrated_suite_property_evaluations": sum(r["checks"] for r in integrated),
    "exhaustive_directional_comparisons": 7998,
    "note": "Execuções separadas e integrada são repetições de validação, não amostras adicionais independentes.",
}
(EVIDENCE / "manifest.json").write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
print("Evidências conferidas: cinco grupos, 30 execuções PBT, 30000 avaliações, zero descartes; hashes registrados.")
