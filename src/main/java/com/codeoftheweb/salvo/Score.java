package com.codeoftheweb.salvo;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;
//vease que se importan todos los paquetes de java que seran necesarios para la ejecucion de dicha clase asi como su comparticion
//con otros elementos del proyecto

@Entity//Este elemento seria como la asignacion de dicha clase a un nicho en la basa de datos del proyecto

public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    //vease que se crea la clase de caracterer publico asignandosele el nombre de Player, y los primeros tres elementos que de ella
    //forman parte(@Id,@...)en su orden asignarian en la database, id, asi como indicar al JPA(Java Persistence API) que use ese
    //id para cualesquiera la funcion que se necesite ejecutar

    private long id;

    //vease que se inicializan las variables id, y una variable de tipo double llamada score , la cual se inicaliza con ese tipo pues los valores que se
    //pudiesen almacenar en ella serian de esa tipologia

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="PlayerID_Score")
    private Player player;
    private Double  score;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="GameID_Score")
    private Game game;
    //vease que simplemente se inicializan dos columnas en la base de datos , para el jugador en especifico , y el juego en especifico en el cual
    //el jugador genero score , y su relacion seria many to one pues un jugador puede tener varios score, lo mismo

     public Score(){}
     public Score(Player player,Game game, Double score){
      this.score=score;
      this.player=player;
      this.game=game;
      player.addScore(this);
      game.addScore(this); }
    //vease que se procede a inicializar el constructor para esta clase haciendose alusion a las variables inicializadas , ademas de la invocacion del
    //metodo addScore, previamente creado en la clase player , el cual importaria todo lo referente a esta instancia Score mediante la extension player
    //emitiendosele como parametro  la instancia Score en si('this')

     ///setters
    public void setId(Long id) {this.id = id;}
    public void setPlayer(Player player) {this.player = player;}
    public void setScore(Double score) {this.score = score;}
    ///getters

    public void setId(long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Long getId() {return id;}
    public Player getPlayer() {return player;}
    public Double getScore() {return score;}
    //vease que se inicializan setters y getters pra todas la variables previamente inicializadas en la clase
}
