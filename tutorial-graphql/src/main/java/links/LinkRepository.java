package links;

import java.util.ArrayList;
import java.util.List;

public class LinkRepository {
    
    private final List<Link> links;

    public LinkRepository() {
        links = new ArrayList<>();
        
        // Datos de relleno
        links.add(new Link("http://um.es", "Universidad de Murcia"));
        links.add(new Link("http://graphql.org/learn/", "GraphQL"));
    }

    public List<Link> getAllLinks() {
        return links;
    }
    
    public void saveLink(Link link) {
        links.add(link);
    }
}