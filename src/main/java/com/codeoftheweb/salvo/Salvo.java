package com.codeoftheweb.salvo;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity//vease que lo primero que se crea despues de haber creado los importadores para la clase es el elemento @Entity, que no seria mas que
       //una clase de java ligera para representar la clase en si dentro de una tabla en una base de datos

public class Salvo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    //vease que se crea la clase de caracterer publico asignandosele el nombre de Player, y los primeros tres elementos que de ella
    //forman parte(@Id,@...)en su orden asignarian en la database, id, asi como indicar al JPA(Java Persistence API) que use ese
    //id para cualesquiera la funcion que se necesite ejecutar

    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_Id")
    private GamePlayer gamePlayer;
    private Integer turn ;
    //vease que en este caso se inicializa la relacion manytoone dada la caracteristica prpia d eesta clase , pes en su logica se asume que
    //un game player puede tener cuantos salvos se estimen de ahi que Many(salvos )to One (Game Player).Acto seguido especifica que se crearia
    //una columna dentro de esta recien creada tabla en la base de datos llamada gameplayerId la cual recogeria los id de los gamplayer que ejecutan
    //los salvos en si para cada juego, ademas de inicializarse otra variable de tipo integer llamada turn la cual recogeria los turnos de disparo para cada
    //uno d elos players en el game en cuestion , ambos parametros quedarian reflejados en la recien construida tabla

    @ElementCollection
    @Column(name="locations")
    private List <String>shootToPositions=new ArrayList<>();
    //casi siempre elementcollections es usado cuando lo que se quiere plasmar en la base de datos es un coleccion de simples types como strings , o numeros
    //ademas de que elemento al cual hace referencia es dependiente en su totalidad de la entidad que lo crea.O sea que al ser renderizado a la clase Salvo
    //tal cuando la misma se borre esta se borra o modifica igualmente.Vease que simplemente se crea una columna en la base de datos para esta colleccion
    //de types y se le asigna por nombre location, y sus celdas serian llenadas con los datos que ofrezca la inicializacion de la variable de tipo List<string>
    //llamada shootToPositions

    public Salvo(){}
    public Salvo(Integer turns,List <String> shootToPositions,  GamePlayer gamePlayer){
        this.turn=turns;
        this.gamePlayer = gamePlayer;
        this.shootToPositions=shootToPositions;
        gamePlayer.addSalvo(this);
    }
    //vease que se inicializan los constructores para la case en si teniendo en cuenta las variables previamente inicializadas , ademas de mediante extension
    //de gameplayer hacer referencia a el metod addSalvo el cual importaria a esa clase (GamePlayer)todo lo referente a salvo en cuestion usando como parametro
    // tod lo creado en salvo como tal , o sea 'this'

    public void setId(long id) {this.id = id; }
    public void setGamePlayer(GamePlayer gamePlayer) {this.gamePlayer = gamePlayer;}
    public void setTurn(Integer turn) {this.turn = turn; }
    public void setShootToPositions(List<String> shootToPositions) {this.shootToPositions = shootToPositions;}

    public long getId() {return id;}
    public GamePlayer getGamePlayer() {return gamePlayer; }
    public Integer getTurn() {return turn;}
    public List<String> getShootToPositions() {return shootToPositions;}
    //public List<String> getShootingTurns() {return shootToPositions;}
    //vease que se crean getters y setters para todas la variales inicializadas previamente en la entidad
}

