# Projeto em Java Swing

O objetivo deste projeto é implementar a aplicação Bookings para ser utilizada pelos funcionários de um hotel para reservar quartos para os hóspedes. Cada quarto de hotel acomoda um número de adultos e um número de crianças, inclui algumas comodidades (Televisão, Acesso à Internet, etc.) e tem um preço por noite. Um hóspede do hotel liga para fazer uma reserva de quarto e o funcionário da receção verifica se há quartos disponíveis. Caso haja quarto disponível, o funcionário regista a reserva no sistema com o nome do hóspede, as datas de entrada e saída, além da quantidade de adultos e crianças hospedados no quarto. Quando chega, o hóspede confirma a reserva na receção e faz o check-in. Para a saída, o hóspede faz o check-out na receção

# Modelo de Dados

Para guardar os dados deve criar as seguintes classes:
 * Room
    * Id (int) – Identificador único do quarto
    * RoomNumber (int) -Número do quarto
    * AdultsCapacity (int) – Número máximo de adultos permitidos no quarto
    * ChildrenCapacity (int) – Número máximo de crianças permitidas no quarto
    * Price (float) – Preço por noite do quarto
 * Booking
    * Id (int) – Identificador único da reserva
    * GuestFirstName (string) – Primeiro nome do hospede
    * GuestLastName (string) – último nome do hospede
    * CheckInDate (Date) – Data de entrada
    * CheckOutDate (Date) – Data de saída
    * NumberOfAdults (int) – Número de adultos na reserva
    * NumberOfChildren (int) – Número de crianças na reserva
    * RoomId (int) – Identificador do quarto da reserva
    * StatusId (int) – Identificador do estado da reserva
 * Status
    * Id (int) – Identificador único do estado
    * State (string) – Descrição do estado (Booked | CheckedIn | CheckedOut | Canceled)

# Vistas de lista e detalhes 

Nesta parte da tarefa, o objetivo é criar uma listagem de todos os quartos e as respetivas vistas de edição. Ao criar as vistas de listagem ou criação/edição é importante criar tudo o que é necessário para que as vistas funcionem, e não apenas o UI.

![Imagem](https://i.imgur.com/jjZHibm.png)
![Imagem](https://i.imgur.com/ZZsQbIh.png)
![Imagem](https://i.imgur.com/BfnW0IS.png)
![Imagem](https://i.imgur.com/MaKodwI.png)

# Criar reserva 

A vista deverá possuir um formulário com todos os inputs do mockup. O título da vista, ao editar deverá exibir o nome do hospede. O campo final do formulário não é um campo de entrada, mas sim um campo informativo. Este deve exibir o número do quarto juntamente com o preço por noite. Este valor só será calculado quando o funcionário selecionar um quarto disponível adequado a esta reserva. Nesse sentido, no fundo da vista, temos dois botões: Obter Quarto Disponível e Reservar Quarto. O primeiro botão acionará uma ação que obterá o quarto disponível mais barato, para as datas e quantidade de adultos e crianças selecionadas no formulário. O segundo botão também irá acionar uma ação, para confirmar a reserva e adiciona-a à listagem de reservas. Para a pesquisa do quarto mais barato para as condições selecionadas pelo funcionário é disponibilizada a classe RoomBookingSystem. Esta classe tem o método searchAvailableRooms que recebe a lista de quartos, a lista de reservas, o número de adultos, o número de crianças, a data de checkin, a data de checkout e o identificador do estado Canceled e devolve a lista de quartos disponíveis ordenados por preço crescente.

# Filtro de pesquisa de reservas

Na vista de reservas devemos permitir a procura pelo nome do hóspede e pelo estado da reserva. Quando não houver filtros aplicados, todas as reservas deverão ser exibidas. No final, devemos ter algo como a imagem seguinte.

![Imagem](https://i.imgur.com/BZR9cC4.png)

# Check-in, check-out e cancelamento de reservas

Nesta parte da tarefa, criaremos a lógica para permitir que o funcionário do hotel faça check-in e check-out de hóspedes e também cancele uma reserva. Isso será feito em várias partes. Primeiro, alteraremos a funcionalidade da vista de detalhe da reserva para ao editar uma reserva existente permita check-in, check-out ou o cancelamento. Em seguida, criaremos uma Homepage, onde ficarão visíveis os Check-ins e Check-outs do dia, com um botão para fazer check-in ou check-out diretamente. Isso também levará a algumas mudanças extras. Alteraremos o formulário na vista de detalhe da reserva para permitir apenas a edição enquanto a reserva não estiver confirmada.

![Imagem](https://i.imgur.com/lxCaakf.png)

# Homepage

Crie a vista da página inicial como o mockup seguinte. Não se esqueça de adicionar esta ao menu da aplicação.

![Imagem](https://i.imgur.com/tMfurt6.png)

Devem ser exibidas duas listas na homepage, uma para as reservas com checkin do próprio dia e outra com as reservas com o checkout no próprio dia.

# Dados de teste

![Imagem](https://i.imgur.com/LMLhhre.png)
