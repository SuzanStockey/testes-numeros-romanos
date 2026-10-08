# Versão com validação de entradas — 8/10/2026

## Política adicionada

RP-03 determina `IllegalArgumentException` para inteiros fora de 1–3999 e strings fora do domínio romano canônico. Inclui `null`, vazio, espaços, minúsculas e símbolos ou formas inválidas. Não há normalização. É uma decisão do projeto, acrescentada depois do experimento original, e não uma exigência atribuída ao Codewars.

O codificador verifica o intervalo antes de converter. O decodificador rejeita `null`, vazio e comprimento acima de 15 (máximo canônico: 3888 = MMMDCCCLXXXVIII), interpreta os símbolos e compara o texto com a recodificação do resultado. Isso impede aceitar grafias como IC ou IIII. O limite também evita acumulação desnecessária em textos grandes. A validação reutiliza o codificador de produção; esse acoplamento é verificado pelos testes direcionais com modelo posicional independente e pela checagem exaustiva, mas continua sendo uma dependência do projeto.

## Execuções observadas

| Grupo | Itens | Resultado |
|---|---:|---|
| Exemplos originais | 69 | Aprovados |
| Infraestrutura dos oráculos | 35 | Aprovados |
| Propriedades originais: 6 × 5 sementes | 30 | 30.000 avaliações primárias aprovadas |
| Exemplos de entradas inválidas e fronteiras | 45 | Aprovados |
| Propriedades de rejeição: 2 × semente 42 | 2 | 2000 avaliações primárias aprovadas |
| **Suíte padrão atual** | **181** | **Zero falhas, erros ou testes ignorados** |
| Exaustiva complementar, separada | 2 | 7998 comparações aprovadas |

JUnit 5.13.4, jqwik 1.9.3, JDK Microsoft 17.0.16, Maven 3.9.11. As 32 execuções de propriedades completaram 1000 checks cada, sem descartes. Repetições de entradas são possíveis. A tabela conta itens de framework, não o número de entradas distintas nem de asserções.

## Histórico e reprodução

Os relatórios das etapas 4 e 5 não foram substituídos: seus 134 itens e a detecção de 4/4 variantes pertencem à versão anterior, preservada no snapshot e no histórico Git. A campanha de quatro defeitos não foi repetida nesta versão. Não transferir a matriz de detecção antiga para a implementação com validação.

Com JDK 17 e Maven configurados, executar:

```powershell
mvn --batch-mode --no-transfer-progress test '-Dtest.reports=resultados/validacao/reports'
mvn --batch-mode --no-transfer-progress test '-Dtest=RomanExhaustiveTest' '-DexcludedGroups=none' '-Dtest.reports=resultados/validacao/exhaustive-reports'
node scripts/summarize-validation.cjs
```

Esses comandos sobrescrevem os relatórios da versão atual; preserve-os se for avaliar outra mudança. Os logs `run.log` e `exhaustive.log` registram esta execução. `manifest.json` contém os hashes de produção, testes e configuração; `groups.csv` contém os totais extraídos dos XML. Os metadados de caminhos auxiliares dos XML de entrega podem ser reduzidos sem alterar os casos de teste.

Para reproduzir os scripts e o experimento originais, utilizar a revisão histórica `78399cf` em uma cópia separada. O runner original recusa código que diverge da base da etapa 4. A consolidação da etapa 5 agora verifica o snapshot do código original; a auditoria de entrega verifica separadamente o snapshot histórico e os hashes da versão atual.
