package com.codeoftheweb.salvo;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;
//vease que se importan todos los paquetes de java que seran necesarios para la ejecucion de dicha clase asi como su comparticion
//con otros elementos del proyecto

@Entity//Este elemento seria como la asignacion de dicha clase a un nicho en la basa de datos del proyecto

public class Player {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
        @GenericGenerator(name = "native", strategy = "native")
    //vease que se crea la clase de caracterer publico asignandosele el nombre de Player, y los primeros tres elementos que de ella
    //forman parte(@Id,@...)en su orden asignarian en la database, id, asi como indicar al JPA(Java Persistence API) que use ese
    //id para cualesquiera la funcion que se necesite ejecutar

    private long id;
    @OneToMany(mappedBy = "player",fetch=FetchType.EAGER)
    Set<Score> scoreSet= new HashSet<>();
    //En este caso se hace una importacion  de la clase Score  mediante inicializacion de una Variable de tipo set, llamandosele scoreSet,
    //la explicacion seria tan simple como que cada jugador es portador de un score propio, por tanto la necesidad de importar a esta clase
    // calesquiera los datos que en la clase score se desarrollen , y la manera perfecta de hacer es mediante la inicializacion de este
    //set iimportador de dicha instancia.la misma vendria renderizada por la relacion que la misma ejerce con la clase Player en si , o sea
    // que un player puede tener varios scores de ahi la relacion inicializada como OnetoMany

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayerSet=new HashSet<>();
    //Vease que al ser ambos (game y player)clases renderizadas a una tercera clase llamada GamePlayer la cual resumiria
    // ambos componentes y demas se establece la relacion la relacion  @OneToMany(One to Many  por que es solo un game
    // a inicializar que podria tener varios jugadores, de ahi este razonamiento, donde se establecen , parametros por
    // defecto, como son el mappedBy , haciendo referencia al elemnto a tener en cuenta a la hora de resalizar el map(game),
    // asi como el tipo de fetch, a realizar para dicho proceso(por defecto).
    //Vease que como segundo elemento se establece la interface Set sobre la clase GamePlayer, asignandosele como nombre a dicha ,
    //variable importada el nombre de gamePlayerSet, y se le asigna cualesquiera los valores que se almacenen en el new HashSet,que de
    //hecho al ser un set ninguno de sus elementos seria repetido.Vease que para esta relacion se hace un set sobre GamePlayer,al
    //cual se le llamria gamePlayerSet, y a ttraves de el se estableceria el camino para determinar que elementos del gamePlayer
    //queremes selecionar para asignarselos a este OneToMany.

    private String userName;
    private String email;
    private String password;
    //segun indicaciones del ejercio solamente se demanda que se determine el username para esta clase, asi como la
    //asignacion por defecto de un id para el mismo , de ahi que entonces se declaren estas dos variables de tipo , long y
    //String respectivamente para la creacion del id y la variable de nombre userName que se le asignaria segun el metodo
    //correspondiente  una fecha de inicializacion.

    public Player() { }
    public Player( String userName,String email, String password){
        this.userName=userName;
        this.email=email;
        this.password=password;
    }
    //Acto seguido se procede a declarar el constructor para esta clase , que tendria el mismo nombre que la misma, aunque se le
    //pasarian los parametros por los cuales seria llamado una vez creada una instancia en cualesquiera otro metodo o clase en este
    //proyecto, vease que en esta caso el parametro a pasar seria userName , asignandosele a la variable privada preciamente  creada al
    //al inicio.

    public void addScore(Score score) {scoreSet.add(score);}
    // Este metodo es inicializado a manera de recibir de la clase Score todo lo referente a la misma , y vease que simplemente su retorno no seria
    //mas que ese Set de elementos obtenidos a accedidos a traves de la variable inicializada.Este metodo es previamente inicializado en  la clase Score
    // como parte del constructor de la misma en la cual se hace referencia a esta clase y se le anade este metodo(addscore), pra aquella clase(Score)
    // siendo (this)

    public Set<Score> getScoreSet() {return scoreSet;}
    //En este caso se establece un getter en especifico para la variable de noombre scoreset, lo cual daria la posibilidad a esta clase de leer
    // cualesquiera el contenido que se almacene en ella proveniente de la clase Score y de esta manera poderlo exportar a otras instancias del
    // mismo proyecto

    public List<Double> getScores(){
        return scoreSet.stream().map(score -> score.getScore()).collect(Collectors.toList());
    }
    //Vease que una vez establecido el Set de objetos provenientes de la clase Score a taves de la inicializacion de la variable setscoreset, ademas
    //de obtrenido mediante retorno de la misma (a traves del anterior metodo esplicado) su retorno, se procede a mapear la misma para obtener entonces
    //los score por jugador mediante metodo getScore accedido mediante funcion lambda

    public void addGamePlayer(GamePlayer gamePlayer) {gamePlayerSet.add(gamePlayer);}
    public Set<GamePlayer> getGamePlayerSet() {
        return gamePlayerSet;
    }
    //este metod se crea como iinclusor de todo lo referente al game player en esta clase , pues como es de saber , todo game tiene un gameplayer
    //de ahi que desde la propia instancia del game player , en su constructor se inicialice un metodo(en este caso addGamePlayer) el cual seria
    // el encargado de portar todo lo referente a ese gameplayer y exportralo a cualesquiera de las instancias en este proyecto mediante funcion
    //add(especificandose  "this", pues es el game player completo lo que se exportaria).vease que una vez explicaod lo anterior , simplemente se
    //retorna el gameplayerset previamente inicializado y mediante funcion add se le adicionaria entonces todo lo importado por la instancia gaeplayer
    //en si.Despues de todo eso simplemente entonces mediante getter , se procede a retornar ese gameplayerset previamente llenado con lo importado de
    //gameplayer a traves de un Set referente a gamePlayer

    public List<Game>getGames(){
        return gamePlayerSet.stream().map(gamePlayer->gamePlayer.getGame()).collect(Collectors.toList());
    }
    //vease que tambien se establece  una especie de getter que como parametro tendria un elemento List que importaria la
    //clase Game, asignadosele el nombre de getGamess(),la misma retornaria cualesquiera el listado de objetos que el parametro
    //gamePlayerSet arroje(parametro previamente inicializado al comienzo mediante la declaracion del Set sobre GamePlayer, y el cual
    //tendria como retorno culaesquiera los resultados del new HashSet).Lo cual nos daria un array de jugadores disponibles para este
    //game en particular

    public Score getScorePerPlayer(Game game){
        return this.scoreSet.stream().filter(score -> score.getGame().equals(game)).findFirst().orElse(null);
    }
    //En este metodo simplemente se accede desde esta clase al score que cada player devenga , y que haciendose alusion
    //al game en cuestion haria un un mapeo sobre su set de scores  en particular.

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
    }

    public void setEmail(String email) {this.email = email;}
    public String getEmail() {return email;}

    public void setPassword(String password) {this.password = password;}
    public String getPassword() {return password;}

    public void setId(long id) {this.id = id;}
    public long getId() {
        return id;
    }
    //vease que se establecen setters y getters para las variables id y date respectivamente , pues dichos elemntos, seran los encargados
    //de que a traves de los mismos se puedan acceder a las vsariables asi como modificarlas desde otras instancias y demas.Vease
    //que aunque la declaracion de los mismos hay veces no es necesaria , yo siempre los inicializo para no omitir cualquier paso ,o no
    //incurrir en errores desconocidos

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", gamePlayerSet=" + gamePlayerSet +
                '}';
    }
}
