package com.codeoftheweb.salvo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
//vease que se importan todos los paquetes de java que seran necesarios para la ejecucion de dicha clase asi como su comparticion
//con otros elementos del proyecto

@Entity//Este elemento seria como la asignacion de dicha clase a un nicho en la basa de datos del proyecto

public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    //vease que se crea la clase de caracterer publico asignandosele el nombre de GamePlayer, y los primeros tres elementos que de ella
    //forman parte(@Id,@...)en su orden asignarian en la database, id, asi como indicar al JPA(Java Persistence API) que use ese
    //id para cualesquiera la funcion que se necesite ejecutar.Esta clase es la encargad ade resumir en si o abarcar cuantas connexiones ,
    //puedan sucederse entre games and players de ahi que interconneccion sea de  muchos a ella o sea many to oneOJO

    private Long id;
    //vease que primero se declara el tipo de variable que asignaria el id para esta clase , que aunque no se especifica en el ejercio,
    // al ser la misma receptora de id, y demas elementos dada su caracteristica de Many to One no necesitaria otro tippo de variable , pues
    // vendria recibiendolas desde sus diferentes 'many'

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;
    private Date date;
    //Vease que se establecen las dos conexiones con las clases que establecian su relacion particular desde su instancias a traves del OneToMany
    //pues ellas Game y Player son instancias que que como caracteristicas asumen esa posibilidad , o sea un jugador, o un game pueden
    //tener cuantos Jugador_Juego, se pudiesen crear para cada una de ellas, pero al llegar a esta instancia  o sea son recibidas por un
    //juego -jugador en especifico, invirtiendose la relacion

    @OneToMany(mappedBy="gamePlayers", fetch=FetchType.EAGER)
    Set<Ship> ships = new HashSet<>();

//    @OneToMany(mappedBy="gamePlayers", fetch=FetchType.EAGER)
//    Set<Score> scores = new HashSet<>();

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    Set<Salvo> salvos = new HashSet<>();
    //Vease que aqui se establece la relacion del GamePlayer y su condicion de asumir cuantos Ships sean necesarios de ahi que en su constructor
    //se inicialice  un variable de tipo Set Importando componentes de la clase Ship llamada ships.Vease qe a diferencia del caso anterior en este caso
    //por default se establece que dicha relacion one to many , sea mapeada por gamePlayer, pues solamente en el juego GamePlayer, los ship serian creados
    //no habiendo JoinColumn u otros parametros.Lo mismo sucede para Salvo perteneciente a la clase Salvo, que a su vez corresponde al listado de salvas
    //que cada jugador tiene a su disposicion en ese especifico game(Game_Player), por tanto su mapeo tambien seria referente al game player

    //Lo anteriormente explicado se pone de manifiesto en este acapite en donde se declaran  las instancias que formarian parte de la base
    //de datos para esta clase de gamePlayer la cual recibiria parametros de game y player clases de ahi su condicion de @ManyToOne tanto,
    //para la instancia creada de la clase Player como para la instancia creada de la clase Game, de ahi que mediante funcion @JoinColumn,
    //dentro de dicha base de datos se crearian columnas de nombre player_id y game_id, que resumirian o abarcarian data proveniente de la clase
    //Player y de la clase game,vease que para ello se inicializan  variables que tienen que ver con ambas clases, y en dependencia de una
    // u otra se le asignan al apartado de player_id(private Player player), o al apartado de game_id(private Game game y private Date date;)
    // respectivamente

    public GamePlayer() { }
    public GamePlayer(Player player, Game game, Date date) {
        player.addGamePlayer(this);
        this.game = game;
        this.date=date;
        this.player = player;
    }
    //Vease que aqui se procede a determinar el constructor para esta clase , en donde confluyen varias clase con puntos en comun, como por
    //ejemplo elemtos de game y elemntos de player , de ahi las variables inicializadas previamente con alusones a los dos subsistemas.
    //vease que se nicializa dicho constructor con tres parametros en donde player, correspondiente a la clase player haria referencia  a todo
    //lo que la misma encierra , y game y date harian lo mismo para la clase GAME.por otra parte se le adiciona o se hace referencia a la clase Player
    //adicionandosele lo que en ese momento fue contruido de ahi el elemento (this)

    public void addShip (Ship ship){
        ships.add(ship);
        ship.setGamePlayers(this);
    }
    public void addSalvo(Salvo salvo){salvos.add(salvo);}

    //En las clases de Ship y Salvo, como bien se explico anteriormrente al ser elementos que se crean unica y exclusivamte despues de creado en si
    //el game player especifico, se debe inicializar un metodo que recogeria todo su contenido en esta clase en si , llamado addShip/Salvo en dependencia
    //de la clase en si , y mediante la misma se retornanria cualesquiera loas caracteristicas que estas clases en si encierran en si, adicionandosele
    //al nuevo game player

    public void setPlayer(Player player) {this.player = player;}
    public void setGame(Game game) {
        this.game = game;
    }
    public void setDate(Date date) {this.date = date;}

    public Player getPlayer() {
        return player;
    }
    public Game getGame() {
        return game;
    }
    public Date getDate() {return date; }
    public Long getId() {return id;}
    //vease que en este apartado se establecen los getters y setters para cada una de las variables inicializadas en el constructor
    // mediante los cuales se actualizaria el valor de la variable(setters) o y se leeria el contenido de las mismas (getters)por cuales
    //quiera las clases en el proyecto de ahi su caracter publico

    public Set<Ship> getShips() { return ships;}
    public Set<Salvo> getSalvos() { return salvos;}
    //vease que en este ultimo caso se establecen getters pertenecientes a las variables llamadas ships y salvos provenientes de las clases
    //Ship y Salvo respectivamente, y que se importaron a este gameplayer como elementos del gameplayer en si mediante su relacion que las mismas
    // tienen para cada gameplayer especifco, pues cada uo de ellos podria asimilar tantas salvos y ships como el juego lo permita, de ahi su relacion
    // y su inicializacion como Onetomany

    @Override
    public String
    toString() {
        return "GamePlayer{" +
                "id=" + id +
                ", player=" + player +
                ", game=" + game +
                ", date=" + date +
                ", ships=" + ships +
                ", salvos=" + salvos +
                '}';
    }
}