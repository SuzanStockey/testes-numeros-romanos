# Roteiro da apresentação

A fala abaixo acompanha os oito slides de `apresentacao.html` e está também nas notas do HTML. O tempo previsto é de seis minutos; confirme a duração em um ensaio.

| Slide | Assunto | Tempo | Acumulado |
|---|---|---:|---:|
| 1 | Testes de um conversor de números romanos | 30 s | 0:30 |
| 2 | Problema e contrato das operações | 40 s | 1:10 |
| 3 | Artigos utilizados e decisões de teste | 50 s | 2:00 |
| 4 | Particionamento de equivalência e valores-limite | 45 s | 2:45 |
| 5 | Propriedades e configuração da geração | 55 s | 3:40 |
| 6 | Experimento com defeitos e resultados | 55 s | 4:35 |
| 7 | Análise das falhas e dos contraexemplos | 50 s | 5:25 |
| 8 | Conclusões e limitações do estudo | 35 s | 6:00 |

## Slide 1 — Testes de um conversor de números romanos

**Tempo previsto: 30 segundos.**

Neste trabalho, apliquei três técnicas de teste a um conversor de números romanos: particionamento de equivalência, análise de valores-limite e testes baseados em propriedades. O problema foi escolhido no Codewars e a implementação está em Java. O objetivo foi comparar os testes com exemplos fixos e os testes de propriedades, observando quais defeitos cada grupo detectava. A fundamentação veio de dois artigos sobre testes baseados em propriedades.

## Slide 2 — Problema e contrato das operações

**Tempo previsto: 40 segundos.**

O conversor tem duas operações. toRoman recebe um inteiro entre 1 e 3999 e produz sua representação romana. fromRoman faz o caminho inverso. A representação deve ser canônica: quatro deve ser escrito como IV, por exemplo. O número 944 combina subtração em três posições e resulta em CMXLIV. O contrato adotado não exige comportamento para zero, negativos, texto vazio ou representações inválidas, porque esse tratamento não está definido no desafio. Os testes se concentram nas entradas válidas e na saída correta de cada operação.

## Slide 3 — Artigos utilizados e decisões de teste

**Tempo previsto: 50 segundos.**

O primeiro artigo é o QuickCheck, de Claessen e Hughes. Ele apresenta propriedades executáveis e geradores aleatórios em Haskell. Uma contribuição importante é mostrar que a distribuição das entradas precisa ser verificada: muitos testes podem explorar apenas casos triviais. O segundo artigo é Property-Based Testing in Practice, de 2024. Ele analisa trinta entrevistas com trinta e uma pessoas na Jane Street e descreve dificuldades com geradores, propriedades e interpretação dos resultados. Os artigos têm métodos diferentes e não demonstram superioridade universal. A partir deles, usei um modelo independente, registrei a geração e introduzi defeitos para avaliar os próprios testes.

## Slide 4 — Particionamento de equivalência e valores-limite

**Tempo previsto: 45 segundos.**

No particionamento de equivalência, selecionei doze casos representativos considerando subtração, magnitude e composição dos símbolos. Por exemplo, 86 representa uma forma aditiva, enquanto 944 combina pares subtrativos. Na análise de valores-limite, selecionei vinte e cinco números, incluindo os extremos do intervalo e vizinhos das mudanças como três, quatro e cinco. Existem três casos compartilhados entre as técnicas. Depois de consolidá-los, restaram trinta e quatro pares. Cada par foi verificado nas duas direções, e um teste de chamadas intercaladas completou os sessenta e nove itens desse grupo.

## Slide 5 — Propriedades e configuração da geração

**Tempo previsto: 55 segundos.**

Foram implementadas seis propriedades. Duas verificam a ida e volta: converter para romano e voltar ao mesmo inteiro, ou fazer o caminho inverso. Outras duas verificam formato canônico e intervalo. As duas restantes comparam cada operação com um modelo independente, construído com tabelas por posição decimal. O algoritmo de produção usa valores decrescentes, então não é a mesma implementação usada como referência. A geração mistura oitenta por cento de valores uniformes com vinte por cento de limites. Usei cinco sementes e até mil avaliações por propriedade. Na implementação correta, as trinta execuções completaram trinta mil avaliações, sem descartes; as entradas podem se repetir.

## Slide 6 — Experimento com defeitos e resultados

**Tempo previsto: 55 segundos.**

O experimento introduziu quatro defeitos, um por vez. O primeiro trocou IV por IIII. O segundo ignorou a subtração na leitura. O terceiro perdeu unidades quando havia zero em uma posição interna. O quarto converteu 3999 como se fosse 3998. Exemplos, propriedades e combinação detectaram os quatro defeitos. A tabela mostra falhas de asserção, não defeitos diferentes: vinte falhas podem corresponder ao mesmo defeito observado por várias propriedades e sementes. A combinação não encontrou uma variante adicional. Depois do experimento, a implementação correta foi restaurada e os cento e trinta e quatro itens da suíte padrão passaram novamente.

## Slide 7 — Análise das falhas e dos contraexemplos

**Tempo previsto: 50 segundos.**

A análise por propriedade mostra uma diferença importante. No terceiro defeito, 1001 virou M. M é uma representação romana válida, por isso a propriedade de formato passou. Porém, representa mil e não mil e um; a comparação com o modelo detectou a diferença para MI. Já no defeito de leitura, IV foi interpretado como seis. Na propriedade de ida e volta, com semente quarenta e dois, a ferramenta reduziu o contraexemplo de 999 para quatro. Isso facilitou identificar a subtração ignorada. Portanto, formato e intervalo isolados não garantem o significado correto, e os contraexemplos ajudam a diagnosticar a falha.

## Slide 8 — Conclusões e limitações do estudo

**Tempo previsto: 35 segundos.**

Neste experimento, exemplos e propriedades detectaram os mesmos quatro defeitos, e a combinação não aumentou a detecção. As propriedades de formato e intervalo precisaram de verificações do valor representado para detectar erros semânticos. O resultado é limitado: usei um domínio pequeno e defeitos artificiais que os exemplos já cobriam. Não medi custo nem produtividade e não posso afirmar superioridade de uma técnica em sistemas industriais. O código, os casos documentados e as evidências estão no repositório indicado no slide.

## Como apresentar

Abra o HTML em um navegador. Use as setas para mudar de slide e F ou F11 para tela cheia. N mostra as notas; elas aparecem na mesma janela, então deixe-as ocultas durante a projeção e use este roteiro em outro dispositivo ou impresso. O mostra todos os slides com as notas para ensaio. Os slides funcionam sem internet; os links externos precisam de conexão.

Ao explicar a tabela do slide 6, destaque que ela conta falhas de asserção, enquanto a detecção foi de quatro defeitos por grupo. Não apresente 30.000 avaliações como 30.000 entradas distintas, nem os resultados como prova de superioridade de PBT.
