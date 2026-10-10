#  Jogo da Nave

Um shoot'em up 2D completo desenvolvido em **Java** (Swing), com fase de inimigos, chefe com partes destrutíveis e sistema de streaming de recursos em tempo real.

##  Sobre o projeto

O jogador controla uma nave, enfrenta ondas de inimigos e, ao atingir a pontuação necessária, luta contra um chefe com **corpo e asas com vida independentes** — cada parte reage visualmente ao dano recebido.

* Sistema de movimentação (teclado e mouse) e tiros
* Inimigos com colisões, explosões e pontuação
* Chefe com 3 zonas de dano: corpo e duas asas (com vida separadas)
* Estados visuais progressivos: nave e chefe mudam de sprite conforme a vida
* Barra de vida animada com 3 estágios (normal, metade, crítica)
* Especial do chefe com área de dano própria
* Animações por sprites (explosões, fumaça, turbina, curto-circuito)
* Menu com animação de fundo e transição suave entre telas
* Tela de derrota animada com retorno automático ao menu
* Efeitos sonoros e música por estado do jogo (fase normal / chefe)
* Ferramenta de debug visual: grid de coordenadas sobre a tela e sobre o chefe

##  Tecnologias

* Java (JDK)
* Java Swing (JFrame, JPanel, Timer, Graphics2D)
* Java Sound API (`javax.sound.sampled`)
* Git e GitHub

##  Habilidades demonstradas

Cada item abaixo está implementado no código :

* **Programação Orientada a Objetos** — classes com responsabilidade única (`Nave`, `Chefe`, `Tiro`, `Inimigo`, `Som`), encapsulamento com getters/setters e constantes nomeadas
* **Programação concorrente** — threads de streaming carregam os quadros de animação em segundo plano com buffers limitados, `synchronized` para acesso às listas compartilhadas e controle correto do ciclo de vida da thread (criada novamente a cada partida, pois threads não podem ser reiniciadas)
* **Gerenciamento de memória em tempo real** — as animações usam mais de 800 imagens no total; carregar tudo de uma vez não é viável, então os quadros entram e saem de buffers circulares conforme são consumidos pelo renderizador
* **Game loop com timers** — separação de responsabilidades entre timers (`Swing.Timer`): lógica (16 ms), animação (50 ms), spawn de inimigos, tiros e ataques do chefe
* **Interfaces gráficas desktop** — `JPanel` customizado com `paintComponent`, troca de telas (menu/jogo) e tratamento de foco entre painéis
* **Tratamento de erros** — captura com mensagens contextuais no `System.err` e stack trace completo nos pontos críticos (carregamento de recursos, áudio, inicialização)
* **Programação funcional (Java 8+)** — uso de `Runnable`, `IntSupplier` e lambdas para inverter dependências sem acoplamento
* **Cálculo e colisão 2D** — deteção de colisão por AABB (`Rectangle.intersects`) com múltiplas zonas de dano no chefe
* **Áudio** — reprodução com controle de volume (dB via `FloatControl`), loops e sons one-shot
* **Organização e documentação de código** — seções comentadas por responsabilidade, padrão consistente de nomenclatura e README funcional


## Como executar
Opção 1 automático:
    apenas baixe o arquivo pelo link abaixo, extraia os arquivos e execute o verificar.bat.
    https://drive.google.com/file/d/1_B8QaE9oXpq1cU8nOkgQH9hjs5XDVqS5/view?usp=drive_link

    Se as dependências (jdk) não estiverem instaladas, ele irá baixar e instalar automaticamente, se escolheres a opção automática.
Opção 2 manualmente:
    Se por algum motivo quiseres baixar as dependências manualmente, instale o jdk manualmente.

Se quiseres executar o jogo em alguma IDE (como o vs code), precisa do jdk instalado para executar.

> Recomendado rodar em tela cheia 1920×1080 (o jogo abre nessa resolução).


