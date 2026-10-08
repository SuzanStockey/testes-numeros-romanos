# Etapa 1 - Fichamentos e comparação crítica

Este documento preserva as notas de leitura da etapa 1, anteriores à implementação e ao experimento. As implicações para o kata registradas aqui são propostas daquele momento; os resultados posteriores estão nos resumos das etapas 4 e 5, e a síntese final está em `resenha.tex`.

As sínteses são paráfrases. As seções e páginas indicadas permitem conferir as afirmações nos PDFs locais. A numeração de páginas abaixo corresponde à posição no PDF, começando em 1, e não necessariamente à paginação dos anais.

## 1. Property-Based Testing in Practice

### Identificação

- **Autores:** Harrison Goldstein, Joseph W. Cutler, Daniel Dickstein, Benjamin C. Pierce e Andrew Head.
- **Publicação:** ICSE 2024.
- **DOI:** https://doi.org/10.1145/3597503.3639581
- **Texto lido:** `../icse24-pbt-in-practice.pdf`, 13 páginas.
- **Página de publicações do autor:** https://harrisongoldste.in/publications/
- **Chave bibliográfica:** `goldstein2024practice`.

### Objetivo e perguntas de pesquisa

O artigo investiga como desenvolvedores experientes usam testes baseados em propriedades (PBT), quais benefícios e dificuldades encontram e que melhorias em ferramentas e métodos poderiam ajudá-los. Pergunta quais são as características de uma cultura madura de PBT em uma empresa (RQ1) e quais oportunidades de pesquisa emergem das necessidades dos desenvolvedores (RQ2). Não é um experimento para comparar quantitativamente a detecção de defeitos de duas técnicas.

**Localização:** introdução, seção 1, pp. 1-2.

### Método e natureza das evidências

Estudo qualitativo na Jane Street, uma empresa com uso intenso de PBT e desenvolvimento principalmente em OCaml. Foram recrutadas **31 pessoas e realizadas 30 entrevistas**, pois uma entrevista foi conjunta. A análise considera 30 unidades de entrevista; por isso, os resultados usam denominadores como 11/30. A distinção evita chamar as 30 entrevistas de 30 indivíduos.

As entrevistas semiestruturadas duravam aproximadamente uma hora. O recrutamento incluiu contato com autores de código que utilizava PBT, anúncio interno e indicação de participantes. O roteiro pediu experiências concretas e foi ajustado ao longo do estudo. Os autores realizaram análise temática, com codificação aberta, validação e revisão dos códigos e uma passagem de codificação axial.

Os grupos descritos são 26 unidades de usuários de testes e 4 de mantenedores da infraestrutura. Esses usuários são desenvolvedores que escrevem testes; a palavra inglesa *tester* não implica necessariamente um cargo exclusivo de QA. O estudo não adotou um critério formal de parada por saturação, embora os autores tenham percebido convergência nas últimas entrevistas.

**Localização:** seções 3.1-3.3, pp. 2-4; Tabela 1, p. 3. O questionário de experiência era opcional: as medianas apresentadas não representam necessariamente todas as pessoas entrevistadas.

### Síntese dos resultados

1. **Confiança e entendimento:** participantes relatam que PBT ajuda a encontrar casos não antecipados e esclarecer o comportamento esperado. Dez das 30 unidades de entrevista relatam defeitos não encontrados por outros métodos. Isso é frequência de relatos na amostra, não uma taxa experimental de superioridade de PBT.
2. **Documentação e revisão:** propriedades também comunicam a especificação. O benefício depende da clareza da propriedade e da compreensão de quem a lê; executar uma propriedade não garante que ela descreva toda a especificação.
3. **Complementaridade:** a comparação com os *expect tests* da Jane Street não revela preferência universal. Exemplos podem comunicar comportamentos concretos com facilidade; propriedades podem condensar relações gerais e automatizar a exploração de entradas.
4. **Propriedades disponíveis facilitam a adoção:** testes diferenciais aparecem em 17/30 unidades; propriedades de ida e volta em 11/30. Essas categorias se sobrepõem e não são percentuais de todos os testes existentes na empresa.
5. **Geradores são parte difícil do trabalho:** é necessário satisfazer pré-condições e obter distribuições úteis. Tanto geradores manuais quanto derivados são mencionados em 19/30 unidades; não são grupos mutuamente exclusivos.
6. **Falha e sucesso exigem interpretação:** reduzir um contraexemplo pode ajudar a depuração, mas o redutor precisa preservar invariantes. Um teste aprovado também precisa ser avaliado: 11 unidades reconhecem atenção insuficiente à eficácia de seus geradores e testes.
7. **Avaliar os próprios testes:** entre as práticas relatadas estão introdução de defeitos/mutação (7/30), inspeção de exemplos gerados (8/30) e cobertura de código (2/30). São práticas dos participantes, não resultados de experimentos executados pelos pesquisadores.
8. **Integração ao desenvolvimento:** testes de propriedades precisam executar rapidamente para acompanhar a suíte habitual. Os relatos de orçamento de tempo variam de 50 milissegundos a 30 segundos; não constituem uma regra de desempenho para toda ferramenta.

**Localização:** benefícios em 4.1, pp. 4-5; comparação em 4.2, p. 5; especificações em 4.3, pp. 5-6; geradores em 4.4, pp. 6-7; falhas e sucesso em 4.5-4.6, pp. 7-8; síntese na Figura 1, p. 8, e seção 5, pp. 8-11.

### Pontos fortes

- Investiga uma prática industrial madura, permitindo estudar dificuldades que persistem mesmo entre usuários experientes.
- Inclui experiências de diferentes equipes e domínios, além de usuários e mantenedores das ferramentas.
- Examina todo o processo: escrever propriedades, gerar entradas, interpretar falhas e avaliar sucessos.
- Explicita limitações do estudo e liga observações a oportunidades de melhoria.

### Limitações e leitura crítica

**Reconhecidas pelos autores (seção 3.3, p. 4):** uma empresa, um ecossistema predominante de linguagem e ferramentas, participantes geralmente experientes, possível viés de lembrança, interesses dos pesquisadores em PBT e ausência de avaliação formal da saturação.

**Nossa interpretação:** o recrutamento seleciona pessoas com experiência em PBT e, em geral, favoráveis à abordagem. O estudo é forte para entender experiências desse contexto, mas não estabelece como iniciantes em Java aprenderão PBT. Relatos de maior confiança e de defeitos encontrados não substituem medições controladas de eficácia, custo ou cobertura. Também não permitem concluir que PBT sempre supera testes por exemplos.

As distinções entre PBT e fuzzing descrevem principalmente os usos e ferramentas discutidos no estudo. Não devem virar uma definição rígida segundo a qual PBT é sempre unitário e fuzzing é sempre de integração.

### Aplicação proposta ao Roman Numerals Helper

O conversor oferece uma relação natural de ida e volta entre codificação e decodificação. Isso justifica sua escolha como cenário em que uma propriedade útil é fácil de formular. Contudo, a simplicidade do kata não reproduz a complexidade dos sistemas industriais entrevistados.

Para evitar a confiança indevida descrita no artigo, propomos: exemplos com respostas conhecidas, inspeção da distribuição de números gerados, atenção aos pares subtrativos e um pequeno experimento com defeitos controlados. Esses procedimentos ainda precisam ser executados e seus resultados registrados.

## 2. QuickCheck: A Lightweight Tool for Random Testing of Haskell Programs

### Identificação

- **Autores:** Koen Claessen e John Hughes.
- **Publicação:** Proceedings of the Fifth ACM SIGPLAN International Conference on Functional Programming (ICFP '00), 2000, pp. 268-279.
- **DOI:** https://doi.org/10.1145/351240.351266
- **Texto lido:** `../quick.pdf`, 12 páginas.
- **PDF disponível:** https://www.cs.tufts.edu/~nr/cs257/archive/john-hughes/quick.pdf
- **Chave bibliográfica:** `claessen2000quickcheck`.

A referência adotada é a publicação original de 2000. Não confundir seu DOI com o de republicações posteriores do artigo.

### Objetivo e contribuição

Apresentar uma ferramenta leve para testar programas Haskell a partir de propriedades executáveis e entradas aleatórias. O desenvolvedor fornece o critério de correção; a ferramenta gera entradas, verifica a propriedade e apresenta um contraexemplo quando ela falha. A contribuição reúne especificações como oráculos e geração aleatória em uma interface integrada à linguagem.

O artigo destaca geradores padrão associados a tipos, combinadores para geradores personalizados, propriedades condicionais, classificação das entradas e controle do tamanho dos dados. Os autores apresentam uma implementação de aproximadamente 300 linhas, uma escolha de simplicidade que limita a instrumentação incorporada.

**Localização:** resumo e introdução, p. 1; seções 2-4, pp. 2-6; conclusão, seção 7, p. 11.

### Método e natureza das evidências

Trata-se de uma apresentação de ferramenta com exemplos, descrição de implementação e relatos de aplicação. A seção 5 reúne unificação, circuitos Lava, demonstração proposicional, impressão de documentos e a biblioteca Edison. Não há ensaio controlado com amostragem de projetos, comparação padronizada de técnicas ou inferência estatística de superioridade.

Portanto, os resultados demonstram usos e problemas observados nos exemplos relatados, mas não fornecem uma estimativa geral do ganho de eficácia ou produtividade.

### Conceitos centrais

| Conceito | Significado | Localização |
|---|---|---|
| Propriedade executável | Relação que deve valer para entradas do domínio e pode ser verificada por código | 2.1-2.2, p. 2 |
| Pré-condição | Restringe as entradas às quais a propriedade se aplica; entradas que não satisfazem a condição são descartadas | 2.3, pp. 2-3 |
| Classificação e coleta | Observam quais categorias de entradas estão sendo efetivamente testadas | 2.4, p. 3 |
| Gerador personalizado | Constrói dados adequados e controla sua distribuição | 3.1-3.2, pp. 3-5 |
| Controle de tamanho | Reduz problemas de terminação e de dados excessivamente grandes em estruturas recursivas | 3.2, pp. 4-5 |
| Oráculo | Critério que decide se o comportamento observado atende ao esperado; pode ser outra implementação ou uma relação geral | 6.2, pp. 9-10 |

Uma propriedade é formulada para todo o domínio, mas uma execução aleatória verifica apenas as entradas produzidas. A quantificação expressa na especificação não transforma amostragem em prova formal.

### Resultados e exemplos que sustentam a leitura

- **Unificação:** não foram encontrados defeitos no unificador desenvolvido pelos autores, mas foram encontradas falhas na especificação, como uma pré-condição de aciclicidade ausente. Escrever a especificação correta pode exigir esforço substancial. Seções 5.1.3-5.1.4, pp. 6-7.
- **Distribuição enviesada:** no exemplo de unificação, mais de 95% dos casos que satisfaziam a pré-condição eram triviais. Um gerador específico reduziu essa proporção para aproximadamente 20-25%. São números daquele exemplo, não taxas gerais de PBT. Seção 5.1.4, p. 7.
- **Circuitos:** testar diferentes tamanhos de entradas revelou casos que verificações feitas para tamanhos fixos não haviam explorado. Seção 5.2, pp. 7-8.
- **Demonstração proposicional:** a comparação entre implementações revelou três defeitos associados a suposições indevidas sobre entradas. Seção 5.3, p. 8.
- **Impressão de documentos:** um relato de Andy Gill descreve a comparação entre modelos e uma implementação Java. Também apresenta uma extensão que procura contraexemplos menores por meio de `smaller`. É importante atribuir essa redução ao relato da seção 5.4, não afirmar que o apêndice já oferece toda a infraestrutura de redução dos frameworks atuais. Seção 5.4, p. 8.
- **Edison:** o relato de menor esforço é uma avaliação pessoal do usuário, não uma medição controlada de produtividade. Seção 5.5, pp. 8-9.

### Pontos fortes

- Explica a ligação entre especificação, oráculo e geração automática com exemplos executáveis.
- Trata explicitamente da distribuição dos dados, dos descartes e de casos triviais.
- Mostra que uma falha pode revelar um erro no programa, na propriedade ou no gerador.
- Apresenta detalhes suficientes para compreender as decisões da ferramenta e suas limitações.

### Limitações e leitura crítica

Os autores reconhecem a ausência de medição integrada de cobertura e a responsabilidade do usuário por investigar a distribuição e decidir se os testes são suficientes (seção 6.6, pp. 10-11). Propriedades precisam ser computáveis; certos comportamentos, como não terminação, têm limitações de observação na implementação apresentada (2.5 e 6.5).

**Nossa interpretação:** os exemplos e depoimentos sustentam a viabilidade da abordagem, mas não autorizam generalizar ganhos de esforço ou taxas de detecção. A discussão sobre vantagens do teste aleatório em 6.1 também recorre a trabalhos anteriores, cujos resultados não foram reproduzidos neste artigo. Não devemos transformar essa discussão em uma conclusão experimental própria de Claessen e Hughes, nem transferi-la automaticamente ao nosso kata.

Geradores e propriedades são código e podem conter defeitos. A possibilidade de descrever uma relação curta não garante que ela caracterize o comportamento completo. A integração originalmente orientada a Haskell também não demonstra, por si só, facilidade de configuração em Java.

### Aplicação proposta ao Roman Numerals Helper

Gerar diretamente inteiros no intervalo de 1 a 3999 evita o custo de produzir valores arbitrários e descartar os que estão fora do domínio. Um gerador de romanos canônicos independente da função de produção poderá explorar o sentido inverso sem usar a implementação como sua própria referência.

A classificação das entradas poderá identificar faixas numéricas, presença de pares subtrativos e transições importantes. Reproduzir falhas por semente e registrar contraexemplos ajudará a depuração. Não precisamos recriar QuickCheck: aplicaremos seus princípios com uma ferramenta de PBT para Java.

## 3. Comparação crítica entre os artigos

| Eixo | Claessen e Hughes (2000) | Goldstein et al. (2024) | Consequência para nosso trabalho |
|---|---|---|---|
| Pergunta principal | Como oferecer teste por propriedades de forma leve? | Como usuários experientes usam PBT e quais dificuldades persistem? | Relacionar fundamentos técnicos à experiência prática |
| Evidência | Ferramenta, exemplos e relatos de aplicação | Entrevistas e análise qualitativa em uma empresa | Separar viabilidade, relatos e medições do nosso experimento |
| Especificação | Propriedades executáveis são oráculos e documentação | Escrever propriedades pode ser difícil; relações disponíveis favorecem a adoção | Escolher o conversor por sua relação de inversão |
| Geradores | Composição, controle de distribuição e tamanho | Construção manual e ajuste de distribuição geram esforço | Usar domínio simples e inspecionar entradas |
| Sucesso dos testes | Muitos casos triviais podem enganar | Desenvolvedores nem sempre verificam a eficácia dos geradores | Avaliar os testes com defeitos controlados |
| Depuração | Relato de redução de contraexemplos | Dificuldades de redução e preservação de invariantes | Documentar a falha e respeitar o domínio válido |
| Rotina de trabalho | Simplicidade e execução leve são objetivos de projeto | Execução rápida é uma exigência relatada pelos usuários | Manter uma suíte reproduzível e de duração prática |
| Generalização | Aplicações selecionadas, predominantemente funcionais | Contexto industrial maduro, principalmente OCaml | Não extrapolar um kata Java para toda a indústria |

### Argumento central para a futura resenha

Os textos convergem na importância de propriedades compreensíveis e de entradas bem distribuídas. Em 2000, esses aspectos aparecem como escolhas técnicas e problemas encontrados ao aplicar a ferramenta. Em 2024, reaparecem como dificuldades práticas de usuários experientes. A comparação sugere que automatizar a geração não elimina a responsabilidade por formular o oráculo e avaliar as entradas que realmente são exercitadas.

Existe uma tensão útil: a proposta original facilita escrever especificações na própria linguagem, enquanto o estudo industrial registra custos que persistem apesar da familiaridade com a ferramenta. Outra tensão é que a liberdade de construir geradores proporciona controle, mas também exige conhecimento e esforço. Isso não constitui uma contradição direta: os artigos têm objetivos, métodos e contextos diferentes.

Nossa posição inicial é combinar exemplos e propriedades. Exemplos ancoram regras específicas; propriedades exploram relações gerais. Essa é uma decisão de projeto motivada pelas leituras, não uma conclusão de que a combinação será empiricamente superior em todos os defeitos.

### Risco concreto da propriedade de ida e volta

Se o codificador emitisse `IIII` para 4 e o decodificador o interpretasse como 4, a ida e volta poderia passar mesmo violando a representação exigida. Esse cenário hipotético justifica combinar a propriedade com exemplos conhecidos e uma verificação independente de forma canônica. Não significa que o Codewars exija rejeitar `IIII` como entrada: seu comportamento para entradas inválidas não será tratado como requisito do kata.

### O que os artigos não estabelecem

- Que PBT substitui testes por exemplos ou provas formais.
- Que aprovação em muitas entradas equivale à correção completa.
- Que cobertura alta garante ausência de defeitos.
- Que os benefícios industriais serão reproduzidos num kata com domínio pequeno.
- Que os dois artigos são manuais específicos de particionamento de equivalência e análise de valores-limite. Essas técnicas serão identificadas separadamente no projeto dos casos.

## 4. Ligação com as próximas etapas

| Decisão proposta | Fundamentação | Evidência a coletar posteriormente |
|---|---|---|
| Formular ida e volta | QuickCheck 2.1/6.2; Practice 4.3 | Contraexemplos e defeitos detectados |
| Manter exemplos conhecidos e limites | Practice 4.2/4.6; interpretação do risco de oráculo incompleto | Quais defeitos cada conjunto detecta |
| Construir entradas válidas diretamente | QuickCheck 2.3/2.4; Practice 4.4 | Entradas efetivas e descartes, se houver |
| Classificar entradas | QuickCheck 2.4; Practice 4.6 e RO6 | Distribuição por categoria |
| Introduzir quatro defeitos controlados | Practice 4.6 | Matriz de detecção por conjunto e execução |
| Registrar configuração e sementes | Necessidade de reprodução do nosso estudo | Número de tentativas, modo de geração, sementes e versões |

O domínio contém 3999 inteiros. Uma ferramenta pode escolher enumerá-lo automaticamente; por isso, será necessário registrar se a execução foi aleatória ou exaustiva. Uma verificação completa da ida e volta ainda só estabelece essa relação no domínio enumerado: não prova, sozinha, que cada saída romana é canônica.

O experimento será pequeno e ilustrativo. Defeitos artificiais, um único sistema e o mesmo autor para código e testes limitarão as conclusões. Para falar de custo, precisaremos registrar o esforço durante o desenvolvimento; quantidade de linhas ou de execuções não mede produtividade por si só.

## 5. Referências e situação da etapa

As duas referências estão em `referencias.bib`, prontas para uso posterior em LaTeX. O registro da busca e o texto para o E5 estão em `pesquisa-e5.md`.

**Etapa concluída:** leitura, fichamentos, comparação crítica e ligação entre literatura e aplicação proposta. A resenha final em LaTeX será escrita após os testes e o experimento, para incluir resultados reais. A apresentação em HTML permanece na etapa de preparação da apresentação.
