# Revisão da etapa 6

Fonte: `resenha.tex`, documento autossuficiente, com referências incorporadas em formato IEEE. O arquivo `pesquisa/referencias.bib` continua disponível para reutilização; não é necessário à compilação desta fonte.

## Conteúdo conferido

- Introdução: kata público, contrato, Java, ferramentas e objetivo.
- Dois artigos acadêmicos citados, sintetizados e comparados, com pontos fortes e limitações dos métodos de pesquisa.
- Aplicação: PE, AVL e PBT; exemplos, seis propriedades, geradores e oráculos.
- Resultados: campanha base, quatro variantes isoladas, contagens por grupo, redução e restauração.
- Discussão: dificuldades, limites de propriedades fracas e ameaças à validade.
- Conclusão: lições proporcionais à evidência, sem alegação de superioridade universal.
- Referências numeradas no estilo IEEE, com DOI dos dois artigos e URL do kata.
- Distinção explícita entre implementação/testes Java e automação auxiliar Python.

As contagens foram confrontadas com `resultados/etapa5/groups.csv`, `detection.csv`, `property-runs.csv` e os resumos das etapas 4 e 5. A apresentação HTML foi posteriormente concluída na etapa 7. Na revisão final, a descrição de PE foi corrigida: são 12 casos representativos, não 12 classes disjuntas.

## Pendências de acabamento e entrega

1. Inserir nome completo e matrícula do estudante no comando `\author`; esses dados foram solicitados durante a etapa 6. A fonte identifica trabalho individual sem inventar autoria.
2. Compilar e revisar visualmente o PDF. A tentativa no compilador integrado retornou `compile-failed`, com o diagnóstico `Unable to find standard directories for platform`. A fonte foi preservada e aberta no editor. Não foi gerado um PDF de entrega.
3. Confirmar 2–4 páginas no PDF compilado e revisar tabelas, referências e quebras. A fonte contém três quebras manuais para organizar quatro páginas, mas esse número ainda não foi verificado e pode variar se houver transbordamento.
4. Depois da publicação do repositório, inserir seu endereço na conclusão, se desejado. Atualmente o texto descreve corretamente a disponibilidade local.

O bloqueio de compilação é do ambiente; não constitui aprovação ou reprovação da sintaxe do documento. Não foi instalado compilador ou plugin adicional.
