package kaviney;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import kaviney.Uzsakymai;
import kaviney.UzsakymaiRepository;

import kaviney.Patiekalai;
import kaviney.PatiekalaiRepository;

import kaviney.TopPatiekalai;
import kaviney.TopPatiekalaiAtaskaita;

import kavinex.*;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/restfull") // This means URL's start with /demo (after Application path)
public class MainController {
	@Autowired // This means to get the bean called userRepository
	           // Which is auto-generated by Spring, we will use it to handle the data
	private UzsakymaiRepository uzsakymaiRepository;
	@Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data	
	private PatiekalaiRepository patiekalaiRepository;
	
	@GetMapping(path="/add") // Map ONLY GET Requests
	public @ResponseBody String naujasUzsakymas (@RequestParam String pav
			, @RequestParam Integer trukme_ruosimo
			, @RequestParam Integer trukme_kaitinimo
			, @RequestParam Double kaina
			, @RequestParam Integer id_patiekalo
			, @RequestParam String klientas			
			) {
		// @ResponseBody means the returned String is the response, not a view name
		// @RequestParam means it is a parameter from the GET or POST request
		
		Uzsakymai n = new Uzsakymai();
		n.setPav(pav);
		n.setTrukme_ruosimo(trukme_ruosimo);
		n.setTrukme_kaitinimo(trukme_kaitinimo);
		n.setKaina(kaina);
		n.setId_patiekalo(id_patiekalo);
		n.setKlientas(klientas);
		n.setBusena( "uzsakytas" );
		System.out.println (n.toString() );
		uzsakymaiRepository.save(n);
		return "Saved";
	}
	
	@GetMapping(path="/changestatus") // Map ONLY GET Requests
	public @ResponseBody String keistiBusena (@RequestParam String busena
			, @RequestParam Integer id 
			) {
		// @ResponseBody means the returned String is the response, not a view name
		// @RequestParam means it is a parameter from the GET or POST request
		
		Optional <Uzsakymai> found = uzsakymaiRepository.findById( id );
		
		// variantas trynimuiui
		// uzsakymaiRepository.deleteById(id);
		
		String res = "Not done";
		
		if ( found.isPresent() ) {
			
			   Uzsakymai n = found.get();
			   n.setId(id);
			   n.setBusena(busena);
			   
			   // pagal https://coderanch.com/t/304851/databases/Java-date-MySQL-date-conversion

			   java.util.Date dt = new java.util.Date();
			   java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			   String currentTime = sdf.format(dt);
			   // \pagal
			   n.setLaikas_patekimo(currentTime);			   
			   uzsakymaiRepository.save(n);	
			   res = "Saved";
			}		
		return res;
	}
		
	@GetMapping(path="/lst-patiekalai")
	public @ResponseBody Iterable<Patiekalai> getAllPatiekalai() {
		// This returns a JSON or XML with the users
		return patiekalaiRepository.findAll();
	}	
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<Uzsakymai> getAllUzsakymai() {
		// This returns a JSON or XML with the users
		return uzsakymaiRepository.findAll();
	}
	
	@GetMapping(path="/allx")
	public @ResponseBody Iterable<Patiekalas> getAllUzsakymaix() {
		// This returns a JSON or XML with the users
		ApplicationContext context = new ClassPathXmlApplicationContext( "file:src/beans.xml" );
		
		UzsakymaiSpring uzsakymai = (UzsakymaiSpring) context.getBean( "uzsakymai" );
		uzsakymai.nuskaityti(); // tik ivedimas
		uzsakymai.ruostiPatiekalus();
		uzsakymai.patiekti();
		;		
		
		return uzsakymai.isnesiotix();
	}
	
	@GetMapping(path="/top-patiekalai")
	public @ResponseBody Iterable<TopPatiekalai> getTopPatiekalai(
			@RequestParam String laikotarpis_nuo
			, @RequestParam String laikotarpis_iki
	) {
		
//		 Map<String, String> properties = new HashMap<String, String>();
//		  properties.put("javax.persistence.jdbc.user", "root");
//		  properties.put("javax.persistence.jdbc.password", "");
//		  EntityManagerFactory emf = Persistence.createEntityManagerFactory("TopPatiekalaiAtaskaita");   // Persistence.createEntityManagerFactory( "jdbc:mysql://localhost:3306/spring_jpa/kavine;user=root;password=");		
		
	    EntityManagerFactory emf = Persistence.createEntityManagerFactory( "TopPatiekalaiAtaskaita" ); // "Eclipselink_JPA" );
		EntityManager entitymanager = emf.createEntityManager();
				
		TopPatiekalaiAtaskaita tp = new TopPatiekalaiAtaskaita( entitymanager );

		return tp.topPatiekalai(laikotarpis_nuo, laikotarpis_iki);
	}	
}
