package com.codeoftheweb.salvo;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity//vease que lo primero que se crea despues de haber creado los importadores para la clase es el elemento @Entity, que no seria mas que
       //una clase de java ligera para representar la clase en si dentro de una tabla en una base de datos
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    //vease que se crea la clase de caracterer publico asignandosele el nombre de Player, y los primeros tres elementos que de ella
    //forman parte(@Id,@...)en su orden asignarian en la database, id, asi como indicar al JPA(Java Persistence API) que use ese
    //id para cualesquiera la funcion que se necesite ejecutar

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_Id")
    private GamePlayer gamePlayers;
    //vease que en este caso se establece la relacion many to one pues al estar en la clase ship , en donde un gamePlayer pudiese tener varias de ellas
    //al comenzar el juego, lo logico seria este tipo de conexion.Vease entonces que se incorpora un nueva columna en esta recien creada base de datos ,
    //llamada gamePlayer_id, pues se haria referencia a cuanas ships tendria ese especifico gameplayer al momento de su creacion.vease que la classe
    //que se renderiza a esta relacion es la de gamePlayer pues es a ella como bien se explico a la que unua vez inicializada , varias ships pudiesen
    //pertenecer

    @ElementCollection
    @Column(name="shipPosition")
    private List<String > shipPositions = new ArrayList<>();
    //Al igual que sucedio con la instancia de Salvo , al ser las ship positions elemtos de tipo simple o type como strings o numeros, no se haria necesario
    //establecer relacion alguna sino que simple es un eleemnto mas de esta clase que no necesita ser mapeado pues ya forma parte de ella mediante notacion
    //menos compleja(type).Vease que se rendieriza la varibale de tipo String en formato List llamada shippositions a este elementcollection, asignandosele
    //cualesquiera el retorno que de un nuevo array list contenedor de sus datos

    private String typeShip;//se inicializa otra variable de tipo string  para definir el tipo de ship

    public Ship(){}
    public Ship(String typeShip, List<String > shipPositions, GamePlayer gamePlayer){
        this.typeShip=typeShip;
        this.shipPositions=shipPositions;
        this.gamePlayers = gamePlayer;
        gamePlayer.addShip(this);
    }
    //vease que se inicializan los constructores para la case en si teniendo en cuenta las variables previamente inicializadas , ademas de mediante extension
    //de gameplayer hacer referencia a el metod addShip el cual importaria a esa clase (GamePlayer)todo lo referente a ship en cuestion usando como parametro
    // tod lo creado en ship como tal , o sea 'this'



    public long getId() {return id;}
    public GamePlayer getGamePlayers() {return gamePlayers;}
    public List<String> getShipPositions() {return shipPositions; }
    public String getTypeShip() {return typeShip; }

    public void setGamePlayers(GamePlayer gamePlayers) {this.gamePlayers = gamePlayers;}
    public void setShipPositions(List<String> shipPositions) {this.shipPositions = shipPositions;}
    public void setTypeShip(String typeShip) { this.typeShip = typeShip; }
    public void setId(long id) {this.id = id;}
//vease que se crean getters y setters para todas la variales inicializadas previamente en la entidad
}
