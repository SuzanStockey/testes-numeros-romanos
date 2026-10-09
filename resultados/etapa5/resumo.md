# Etapa 5 — experimento com defeitos controlados

## Procedimento e integridade

Foram executadas quatro variantes isoladas do conversor, cada uma derivada da mesma implementação aprovada na etapa 4. Para cada variante foram executados os exemplos, as propriedades e a combinação dos dois grupos. Nenhum teste foi alterado para acomodar os defeitos. A infraestrutura e a checagem exaustiva não integram os grupos comparados.

O script `scripts/run-experiment.py` registra comandos, snapshots, diferenças de código, logs e XML do Surefire. Uma enumeração auxiliar dos 3999 pares confirma que cada variante realmente altera entradas válidas; ela serve para validar o defeito, sem atribuir sua detecção ao PBT. O código foi restaurado byte a byte em bloco `finally`, recompilado e submetido à suíte padrão: **134 itens aprovados, zero falhas, erros ou testes ignorados**. Os hashes dos 14 arquivos avaliados na etapa 4 continuaram iguais. SHA-256 do conversor restaurado: `65a0a227484c990d3bce7399177d59795c5727ce60582d57552f393dbd66ed62`.

Ambiente: JDK Microsoft 17.0.16, Maven 3.9.11, JUnit 5.13.4 e jqwik 1.9.3. Foram mantidas as cinco sementes 42, 2024, 2026, 3999 e 104729, seis propriedades e até 1000 avaliações por propriedade/semente. Geração aleatória com mistura 80/20 de distribuição uniforme e limites, casos especiais automáticos e banco de falhas desabilitados.

## Comparação observada

Um grupo detecta uma variante quando ao menos uma asserção falha por causa da alteração. Os números abaixo contam itens reportados pelo framework, não entradas distintas nem defeitos diferentes.

| Variante | Alteração | Exemplos: falhas/69 | PBT: falhas/30 execuções | Combinação: falhas/99 | Sementes com detecção por PBT |
|---|---|---:|---:|---:|---:|
| DF-01 | Trocar `IV` por `IIII` no codificador | 3 | 20 | 23 | 5/5 |
| DF-02 | Somar os símbolos sem subtração no decodificador | 21 | 20 | 41 | 5/5 |
| DF-03 | Suprimir unidades quando há zero em posição interna | 5 | 15 | 20 | 5/5 |
| DF-04 | Codificar 3999 como se fosse 3998 | 1 | 15 | 16 | 5/5 |

**Detecção: exemplos 4/4, PBT 4/4 e combinação 4/4.** A combinação não acrescentou defeitos detectados nesta amostra. Suas falhas corresponderam à soma das falhas dos dois grupos separados. Os exemplos incluem 68 comparações direcionais dos 34 pares e um teste de sequência; a sequência pode falhar uma vez, mesmo contendo várias chamadas.

### Detecção por propriedade

Cada célula indica quantas das cinco sementes produziram falha naquela propriedade.

| Variante | P01: ida/volta inteiro | P02: ida/volta romano | P03: formato | P04: intervalo | P05: codificador/modelo | P06: decodificador/modelo |
|---|---:|---:|---:|---:|---:|---:|
| DF-01 | 5 | 5 | 5 | 0 | 5 | 0 |
| DF-02 | 5 | 5 | 0 | 5 | 0 | 5 |
| DF-03 | 5 | 5 | 0 | 0 | 5 | 0 |
| DF-04 | 5 | 5 | 0 | 0 | 5 | 0 |

DF-03 e DF-04 preservam o formato canônico, mas alteram o significado numérico; P03 não os detectou. DF-02 não afeta o codificador, portanto P03/P05 continuaram aprovadas; P04 falhou porque algumas somas ultrapassaram 3999. P04/P06 aprovaram os defeitos exclusivos do codificador. Assim, aprovação de uma propriedade fraca ou de uma operação não garante correção do conversor inteiro. P01 inclui guarda de formato e P02 inclui guarda de intervalo, conforme o contrato documentado.

## Contraexemplos e redução

| Variante | Demonstração direta da variante | Esperado | Observado | P01, semente 42: original → final | Passos de redução reportados |
|---|---|---|---|---|---:|
| DF-01 | `toRoman(4)` | `IV` | `IIII` | 2504 → 4 | 1 |
| DF-02 | `fromRoman("IV")` | 4 | 6 | 999 → 4 | 3 |
| DF-03 | `toRoman(1001)` | `MI` | `M` | 2504 → 901 | 3 |
| DF-04 | `toRoman(3999)` | `MMMCMXCIX` | `MMMCMXCVIII` | 3999 → 3999 | 1 |

Os contraexemplos finais são os fornecidos pelo jqwik, sem alegação de mínimo global. Em DF-03, 901 também perde a unidade: a posição das dezenas é zero. Em DF-04 o valor final permaneceu 3999, apesar do passo registrado pela ferramenta. Para propriedades de pares, a redução preserva a associação entre número e romano canônico. Todas as amostras originais/finais disponíveis e mensagens de asserção estão nos CSV e logs.

| Variante | Avaliações primárias reais no grupo PBT |
|---|---:|
| DF-01 | 10.212 |
| DF-02 | 10.116 |
| DF-03 | 15.060 |
| DF-04 | 16.638 |

Cada execução com falha encerrou a geração antes de 1000 avaliações; as aprovadas completaram 1000. Não houve descartes. Os totais são a soma do campo `checks` dos relatórios, excluindo avaliações adicionais de redução. As estatísticas de classificação no log podem incluir chamadas durante a redução e não devem substituir esse campo. Na implementação restaurada, as 30 execuções completaram novamente 30.000 avaliações primárias.

## Validação dos defeitos e limites da conclusão

A enumeração auxiliar encontrou discrepâncias de codificação/decodificação, respectivamente: DF-01 400/0; DF-02 0/1952; DF-03 594/0; DF-04 1/0. Esses números descrevem o alcance das alterações em relação ao modelo, não são taxas de detecção dos grupos.

Os quatro defeitos são artificiais, escolhidos em requisitos já cobertos pelos exemplos. O domínio é pequeno e o gerador favorece limites; DF-04, por exemplo, está explicitamente na tabela de limites. A amostra não sustenta generalizações sobre superioridade, custo ou eficácia em sistemas reais. Cinco sementes aumentam a evidência de repetibilidade desta campanha, mas não constituem cinco estudos independentes. Propriedades com o mesmo gerador e semente reutilizam sequências; avaliações podem repetir entradas.

Os modelos e guardas também podem conter erros. Sua validação por exemplos literais e a diferença entre os algoritmos de produção e referência reduzem esse risco, sem eliminá-lo. Tempos nos XML são registros de execução, sem controle suficiente para comparação de desempenho. A interpretação compatível com os artigos é a complementaridade entre exemplos bem escolhidos, propriedades semanticamente fortes e geradores atentos ao domínio; o experimento evidencia isso, sem provar uma vantagem universal.

## Evidências e reprodução

- `experiment.json`: comandos completos, hashes, resultados individuais e restauração.
- `groups.csv`: totais por variante/grupo.
- `detection.csv`: detecção por propriedade nas cinco sementes.
- `property-runs.csv`: checks, descartes, sementes, amostras e redução, inclusive combinação e restauração.
- `counterexamples.csv`: falhas e mensagens dos exemplos e propriedades.
- `DF-01` a `DF-04`: snapshot, `change.diff`, `validation.json` e relatórios de cada grupo.
- `baseline/restored`: execução final da implementação correta.
- `probe/ExperimentProbe.java`: enumeração auxiliar e demonstrações diretas.

Para reproduzir a campanha, use a revisão 78399cf em uma cópia separada e aplique uma variante por vez, mantendo os testes iguais. Execute com Maven o grupo de exemplos, o grupo de propriedades e a combinação. Os comandos completos utilizados estão em `experiment.json`. Preserve os registros existentes antes de repetir. Falhas de asserção nas variantes são esperadas; erros de compilação não contam como detecção.
