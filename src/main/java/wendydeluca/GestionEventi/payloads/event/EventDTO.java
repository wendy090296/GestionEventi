package wendydeluca.GestionEventi.payloads.event;

public record EventDTO (String title,
                        String description,
                        String location,
                        String date,
                        int availableSeats){
}
