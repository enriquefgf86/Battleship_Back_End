package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ElementCollection;
import javax.persistence.Column;

import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.*;
//vease que se importan todos los paquetes de java que seran necesarios para la ejecucion de dicha clase asi como su comparticion
//con otros elementos del proyecto

@Entity//Este elemento seria como la asignacion de dicha clase a un nicho en la basa de datos del proyecto

public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    //vease que se crea la clase de caracterer publico asignandosele el nombre de Game, y los primeros tres elementos que de ella
    //forman parte(@Id,@...)en su orden asignarian en la database, id, asi como indicar al JPA(Java Persistence API) que use ese
    //id para cualesquiera la funcion que se necesite ejecutar

    private long id;
    private Date date;
    //segun indicaciones del ejercio solamente se demanda que se determine la fecha de creacion de cada juego , asi como la
   //asigancon por defecto de un id para el mismo , de ahi que entonces se declaren estas dos variables de tipo , long y
   //Date respectivamente para la creacion del id y la variable de nombre date que se le asignaria segun el metodo
   //correspondiente  una fecha de inicializacion.

    @OneToMany(mappedBy="game", fetch= FetchType.EAGER)
    Set<GamePlayer> gamePlayerSet=new HashSet<>();
    @OneToMany(mappedBy="game", fetch= FetchType.EAGER)
    Set<Score> scoreSet=new HashSet<>();
    //Vease que al ser ambos (game y player)clases renderizadas a una tercera clase llamada GamePlayer la cual resumiria
    // ambos componentes y demas se establece la relacion la relacion  @OneToMany(One to Many  por que es solo un game
    // a inicializar que podria tener varios jugadores(2), de ahi este razonamiento, donde se establecen , parametros por
    // defecto, como son el mappedBy , haciendo referencia al elemnto a tener en cuenta a la hora de resalizar el map(game),
    // asi como el tipo de fetch, a realizar para dicho proceso(por defecto).
    //Vease que como segundo elemento se establece la interface Set sobre la clase GamePlayer, asignandosele como nombre a dicha ,
    //variable importada el nombre de gamePlayerSet, y se le asigna cualesquiera los valores que se almacenen en el new HashSet,que de
    //hecho al ser un set ninguno de sus elementos seria repetido.Vease que para esta relacion se hace un set sobre GamePlayer,al
    //cual se le llamria gamePlayerSet, y a traves de el se estableceria el camino para determinar que elementos del gamePlayer
    //queremes selecionar para asignarselos a este OneToMany.
    //El mismo caso se repetiria para el score , pues un juego podra tener varios Scores, de ahi que la relacion de ese juego
    //con los scores sea de One to Many, y al igual que el caso anterior se resumiria dicha informacion en una variable referente
    //a la clase Score , en donde para cada jugador  se generaria un set de anotaciones designandosele por nombre scoreset

    public Game(){};
    public Game( Date date) {this.date = date;}
     //Acto seguido se procede a declarar el constructor para esta clase , que tendria el mismo nombre que la misma, aunque se le
     //pasarian los parametros por los cuales seria llamado una vez creada una instancia en cualesquiera otro metodo o clase en este
     //proyecto, vease que en esta caso el parametro a pasar seria Date , asignandosele a la variable privada preciamente  creada al
     //al inicio .

    public List<Double> getScores(){
        return scoreSet.stream().map(score -> score.getScore()).collect(Collectors.toList());
    }

    public List<Player>getPlayers(){
        return gamePlayerSet.stream().map(gamePlayer->gamePlayer.getPlayer()).collect(Collectors.toList());
    }
    //vease que tambien se establece  una especie de getter que como parametro tendria un elemento List que importaria la
    //clase Player, asignadosele el nombre de getPlayers(),la misma retornaria cualesquiera el listado de objetos que el parametro
    //gamePlayerSet arroje(parametro previamente inicializado al comienzo mediante la declaracion del Set sobre GamePlayer, y el cual
    //tendria como retorno cualesquiera los resultados del new HashSet).Lo cual nos daria un array de jugadores disponibles para este
    //game en particular, este array de jugadores seria facilmente extraible a traves de un lambda funcion que entonces mapearia dicha
    //gameplayerset retornando de dicho objeto el filtro de players  a traves del metodo getPlayer.
    //El mismo proceso se repetiria  para obtener un listado de Scores proveniente de la clase Score

    public void addScore(Score score) {scoreSet.add(score);}
    public Set<Score> getScoreSet() { return scoreSet;}

    public void addGamePlayer(GamePlayer gamePlayer) { gamePlayerSet.add(gamePlayer);}
    public Set<GamePlayer> getGamePlayerSet() {return gamePlayerSet;}
    //este metod se crea como iinclusor de todo lo referente al game player en esta clase , pues como es de saber , todo game tiene un gameplayer
    //de ahi que desde la propia instancia del game player , en su constructor se inicialice un metodo(en este caso addGamePlayer) el cual seria
    // el encargado de portar todo lo referente a ese gameplayer y exportralo a cualesquiera de las instancias en este proyecto mediante funcion
    //add(especificandose  "this", pues es el game player completo lo que se exportaria).vease que una vez explicaod lo anterior , simplemente se
    //retorna el gameplayerset previamente inicializado y mediante funcion add se le adicionaria entonces todo lo importado por la instancia gaeplayer
    //en si.Despues de todo eso simplemente entonces mediante getter , se procede a retornar ese gameplayerset previamente llenado con lo importado de
    //gameplayer a traves de un Set referente a gamePlayer.
    //Vease que el mismo proceso se repite  para obtener el score o set de scores para cada jugador


    public void setDate(Date date) {this.date = date;}
    public Date getDate() {return date;}

    public void setId(long id) {this.id = id;}
    public long getId() {return id;}
    //vease que se establecen setters y getters para las variables id y date respectivamente , pues dichos elemntos, seran los encargados
    //de que a traves de los mismos se puedan acceder a las vsariables asi como modificarlas desde otras instancias y demas.Vease
    //que aunque la declaracion de los mismos hay veces no es necesaria , yo siempre los inicializo para no omitir cualquier paso ,o no
    //incurrir en errores desconocidos


    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", date=" + date +
                ", gamePlayerSet=" + gamePlayerSet +
                '}';
    }
}
