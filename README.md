# Princess vs Donkey Kong - Jogo 2D em Java (POO)
### ISCTE-IUL | Programação Orientada a Objetos

Este repositório contém o código-fonte de um jogo 2D *top-down* desenvolvido em Java, focado na exploração de masmorras (Dungeon Crawler). O projeto foi desenhado com uma forte aposta em **arquitetura de software** e **Padrões de Desenho (Design Patterns)**, garantindo um código altamente modular, expansível e fácil de manter.

---

##  Mecânicas de Jogo e Funcionalidades

O jogador controla a "Princess" e tem como objetivo navegar por várias salas (Rooms), derrotar inimigos e encontrar a bandeira final (`Flag`), superando obstáculos e gerindo recursos e tempo.

* **Combate e Espadas:** O jogador possui HP (Vida) e Dano de Ataque. Apanhar espadas (`Sword`) **aumenta permanentemente o dano de ataque**, sendo essencial para derrotar inimigos mais fortes com menos golpes.
* **Sistema de Durabilidade (Carne/Meat):** Implementação de um sistema de tempo global (ticks). A carne cura o jogador, mas **apodrece após um curto período de tempo**. O jogador precisa de planear a sua rota e "comê-la" rapidamente antes que se estrague.
* **Sistema de Bombas (AoE):** O jogador pode apanhar bombas espalhadas pelo mapa e guardá-las. Pressionando a tecla **'B'**, a bomba é largada no chão. Após alguns turnos, explode num raio de área definido, destruindo obstáculos, ferindo inimigos e ativando armadilhas em cadeia.
* **Armadilhas Dinâmicas:** Existência de armadilhas visíveis (`Trap`) e armadilhas ocultas (`HiddenTrap`) que são ativadas quando o jogador pisa o bloco, exigindo navegação cuidadosa.
* **Inteligência Inimiga Variada:**
  * **Donkey Kong (`Enemy`):** O "Boss" que precisa de ser derrotado para abrir as portas da sala (`Door`).
  * **Morcegos (`Bat`):** Movimentação aleatória e imprevisível pela grelha.
  * **Bananas:** Obstáculos em queda vertical.

---

## Arquitetura de Software e POO

O verdadeiro valor deste projeto reside na sua infraestrutura técnica. Foram aplicados os pilares da Programação Orientada a Objetos para criar um sistema expansível:

### 1. Padrões de Desenho (Design Patterns)
* **Singleton:** Utilizado na `GameEngine` e na `ImageGUI` para garantir um único ponto global de controlo de estado do jogo e renderização gráfica.
* **Observer:** A `GameEngine` atua como *Observer* da interface gráfica (`ImageGUI`), reagindo de forma assíncrona aos inputs do teclado (eventos).
* **Factory Method:** Instanciação dinâmica de classes a partir da leitura de carateres num ficheiro `.txt` (ex: 'H' = Player, 'W' = Wall).

### 2. Polimorfismo e Interfaces
A base de código foi estruturada utilizando interfaces de contrato estritas:
* `Attackable`: Partilhada entre o Jogador e os Inimigos, definindo os métodos de receber dano e estado de derrota.
* `Interactable`: Define como o jogador interage com o ambiente (apanhar itens, pisar armadilhas).
* `TicksChangeble`: Interface crucial para a mecânica de tempo, aplicada a objetos que sofrem mutações com a passagem de turnos (como a carne a apodrecer).
* `FallableTile`: Define o comportamento físico de objetos que permitem queda.

### 3. Hierarquia de Classes Abstratas
Criação de classes base como `MoveableObject` (que trata da lógica vetorial e colisões genéricas) e `InteractableObjects`, permitindo que novas entidades sejam adicionadas ao jogo sem duplicar código (Princípio Open/Closed do SOLID).

---

## Tecnologias Utilizadas

* **Linguagem:** Java (JDK 8+)
* **Bibliotecas:** Interface Gráfica nativa baseada em `javax.swing` e `java.awt` (fornecida pela framework académica `pt.iscte.poo`).
* **Estruturas de Dados:** Uso intensivo de *Collections* (Lists, ArrayLists) e iterações funcionais (`forEach`) para gestão espacial de objetos.

---

## Como Executar o Jogo

### Pré-requisitos
* Java Development Kit (JDK) instalado no sistema.
* Um IDE (como Eclipse, IntelliJ IDEA ou VS Code com extensão Java).

### Instalação e Execução
1. Clone este repositório para a sua máquina local.
2. Abra o projeto no seu IDE.
3. Garanta que a pasta `images` (contendo os sprites visuais) e os ficheiros de mapa (`room0.txt`, etc.) estão na raiz do projeto.
4. Execute o ficheiro principal da aplicação: `Main.java`

### Controlos
* **Setas Direcionais:** Movimentar a personagem (o combate e apanhar itens são feitos automaticamente ao chocar contra eles).
* **Tecla 'B':** Largar a bomba no chão (após ter apanhado uma).

### Game Visual

<img width="477" height="524" alt="Captura de Tela 2026-06-11 às 02 30 33" src="https://github.com/user-attachments/assets/2a0ccd7e-69c2-4e30-806a-66496b07549b" />
 
