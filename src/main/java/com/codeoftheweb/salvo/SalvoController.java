package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
//vease que se importan todas las librerias necesarias para el desarrollo de esta parte del proyecto

@RestController//El resController no seria mas que el controlador y determinador de metodos que definirian o harian
//los data request, donde opcionalmente podrian modificar dicha data en las bases de datos y finalmente
//retornarian un objeto que podria ser converso a JSON notacion listo para enviar
//@RequestMapping("/api/games")
public class SalvoController {//vease que se inicializa la clae especificandose el nombre del proyecto y el tipo de clase 'Controller'
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private ShipRepository shipReposotiry;
    @Autowired
    private SalvoRepository salvoRepository;
    //vease que en este caso se hace alusion a los  respositorios creados previamente para cada una de las clases,y se iniciaizarian
    //despues de un AUtowired que no seria mas que un a inyeccion de dependencias u objetos , o o sea  que consiste que en lugar
    // de que cada clase tenga que instanciar los objetos que necesite, sea Spring el que inyecte esos objetos, lo que quiere decir
    // que es Spring el que creara los objetos y cuando una clase necesite usarlos se le pasaran (como cuando le pasas un parámetro
    // a un método).
    //Vease que como ultimo tambien se anade un Autowired pra encryptar cualesquiera la password que el usuario aloja en su constructor(en
    //este caso en el apartado de player.Este ultimo autowired provine del proceso inicializado en la Aplicacion class(SalvoAplication) para generar
    // un metodo de autentificacion mediante el core de configuration(@Configuration) ,que aparte del Bean principal fue creado para este proposito
    //en el mismo se establecen las pautas que se deben seguir para establecer las configuraciones d eseguridad y demas , mediante importacion de metodo
    //GlobalAuthenticationConfigurerAdapter como extension(extends) de la clase creada para ese proposito en si alla en el SalvoAplication(
    //BattleShipSecurityConfiguration)en donde entonces se inicializa un Autowired contenedor de un un repositorio referente a cualesquiera de las
    // clases en donde el password se alojara , y su alusion sa de que manera mediante este repositorio se buscaria por el usuario poseedor de este password en
    // si, donde en este caso seria el repositorio del player , en donde si nos remitimos al mismo veremos que en su query method, determina que dicho
    // player seria queried mediante encontrar su userName(findByUserName), haciendose alusion al campo previamente creado en el constructor del player
    // (string userName), de ahi que entonces se analizara la base de datos mediante este concepto y se inicializaran los parametros de seguridad
    //para ese usuario en cuestion en caso de que el mismo exista.Vease que estos parametros de seguridad no serian mas que un metodo dentro de la clase
    //BattleShipSecurityConfiguration de tipo init, en el cual se demarcarian excepciones y metodos previamente creados por SpringBoot, los cuales demarcarian
    //y demandarian , primero que se inicialice como parametros de ese metodo una variable de tipo AuthenticationManagerBuilder, la cual por voluntad le
    // llame authUser, en donde entonces se inicializarian una serie de pasos para determinar los listados de permisos segun el tipo de usuario, asi como
    // los diferentes getter correspondientes a la clase que en si contien el password al cual se renderiza este metodo de seguridad como parametros de
    //confirmacion o chequeo para que el authentication sea veridico.Ver esto mas explicado en el Salvo Aplication

    @RequestMapping("/api/game")
    public Map<String,Object> getGame(  Authentication authentication) {
        Map<String,Object>dto=new HashMap<>();
        dto.put("games",gameRepository.findAll().stream().map(game->makeGameDTO(game)).collect(Collectors.toList()));
        if(authentication==null){
            dto.put("player",null);
        }
        else{
            dto.put("player",makePlayerDTO(playerLoggedDetails(authentication)));
        }
        return dto;
    }
    //vease que se inicializa una primera clase publica en donde a groso modo se empieza a disenar lo que seria una especie de
    //API a la cual se harian llamadas desde el front-end para obtener estos elementos mapeables con estructura , string , objeto
    //por indicaciones del mismo plan de ataque se inicializa con el camino ('api/game'), y que como parametro se le especifica
    //el componente de Spring Security Authentication authentication , puesto que una vez blindada la aplicacion con este tipo de seguridad
    // la entrada a cualquier informacion referente a games , o gamplayer , seria a traves de los players, y para que un player en si pueda
    // acceder entonces necesitaria tener aprobacion de un componente de seguridad que le de acceso a la informacion de la aplicacion,
    // que en este caso seria Authentication.
    //Explicado lo anterior el primer elemento de la conformacion de API mediante la creacion de data ttransference object(dt0), seria
    //seria el apartado de juegos 'games', en donde segun el diseno del objeto mapeable mediante String y object, se determina que se llamara
    //games , y el objeto estaria conformado por el acceso a games mediante su repositorio,especificandose que como hook de acceso se inicializaria
    //el metodo findAll(), vease que se escoge este metodo pues lo que se necesita es un listado de games para este Api,y este metodo lo facilita
    //donde fundamentado por la otra funcion llamada stream(), que no seria mas que el procesador para colleciones de objetos se mapearia mediante
    //funcion arrow(lambda en este caso)el dto construido posteriormente para game(makeGameDto), para posteriormente ser collectado y procesado como
    //resultado en una nueva lista de games , contenedora del MakeGameDto en donde se despliegan los datos del API en detalle.
    //Vease que entonces en la tercera parte se procedria a iniciar el proceso para determinar el player que tendria acceso a esta informacion de los
    //games, donde para eso necesitaria tener o pasar por ciertos procesos de seguridad dada la utilizacion del spring security en la aplicacion,
    //de ahi que antes de empezar a construir el dto , se procederia a verificar si el player que pretende acceder existe o esta autentificado,
    //de ahi que se establezca una condicionante que controle dicho problema, especificandose , que si el elemento de seguridad authentication
    //es null , entonces el elemnto dto, que conformaria la segunda parte de este API(de nombre 'players'), seria null; en caso de que no ,
    // entonces se pasaria a conformar el dto para el apartado de players, en donde simplemente se accederia al dto posteriormente creado
    //para el player MakePlayerDto, el cual tendria como parametros ese jugador previamente autentificado , el cual entonces ya tendria permiso para
    //acceder a la informacion, donde para eso , simplemente se hace un llamdo a otro metod complemetnario llamado 'playerLoggedDetails', el cual se crea
    //abajo tambien y tendria como funcion inicializar los parametros de autentificacion de Spring-security, para el Player en cuestion que pretende
    //acceder

    private Map<String, Object> makePlayerDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id",player.getId());
        dto.put("player", player.getUserName());
        dto.put("email",player.getEmail());
        return dto;
    }
    //este es uno de los metodos complementarios para esta primera metodo conformante del API api/game, en donde como bien lo dice su
    // encabezado se pasaria a conformar un objeto mapeable de estructura String, Object llamdo makePlayer Dto, en donde , como parametros de
    //acceso se tendria la clase Player , y su elemnto constructor como referencia  a los elementos que la misma posee para conformar la
    //secuencia de datos.Vease que segtun las especificacion del ejercio se determina que dicho dto , pasaria datos referentes al id , del
    //jugador , su nombre , y su email, todos ellos accedidos mediante el parametro de Player (player) el cual nos daria acceso a la clase en si
    // en donde entoces los datos serian facilitados por los getter que en su momento fueron inicializados en la misma , de ahi los getId,getUserName
    //y getEmail respectivamente.Una vez puestas estas estructuras de datos en los objetos transferibles mediante dto.put, simepre tendiendo en cuenta
    //la estructura <String,Objet>, se retornaria dicho dto, para cualesquiera su uso(en este caso vease que se usa en el metodo publico anterio

    private Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id",gamePlayer.getId());
        dto.put("player", makePlayerDTO(gamePlayer.getPlayer()));
        return dto;
    }
    //el segundo metodo complementario para el primer RequestMapping al api/game seria este dto  correspondiente a gamePlayer, de ahi su nombre
    // makeGamePlayerDTo,segun los parametros que se pasarian para su conformacion , vease que se requiere primero un id , para ese gamePlayer
    //en si , de ahi que a traves del mismo se acceda a su getter (getId), previamente inicializado en la clae de gamePlayer, y el segundo parametro
    //seria el jugar que en si interactua con ese game formando el binomio gamePlayer , de ahi que se solicite el player, y para acceder al
    //mismo simplemente se convoca al previamente inicializado makePlayeDTO, el cual traeria todos los parametros necesarios para desarrollar
    //el json con los datos del jugar , y traves del mismo , entonces se llamria al parametro gamePlayer, para entonces obtener el getter que
    //facilita el jugador con interactua en este game(vease que el clase de GamePlayer, en el constructor de la misma , mediante el parametro
    //player se adiciona el contexto gamePlayer  en si (player.addGamePlayer(this)pues como jugador solo seria imposible acceder a el, a no ser
    //  que se invoque mediante la clase que establece la conexion entre el mismo y el game en cuestion(o sea el gamplayer), y despues a traves de
    //  ella acceder al player mediante un getter (getPlayer)asequible desde el primer momento en que en la clase Gamplayer se hizo la extension
    //  al metodo addGamePlayer, mediante el parametro player en si(ver clase GamePlayer (constructor)))

    private Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id",game.getId());
        dto.put("init_date", game.getDate());
        dto.put("players_in_game",game.getGamePlayerSet()
                .stream()
                .sorted((gp1,gp2)-> gp1.getId().compareTo(gp2.getId()))
//         .sorted(Comparator.comparing(GamePlayer::getId))
                .map(gamePlayer->makeGamePlayerDTO(gamePlayer)));
        return dto;
    }
    //el ultimo complemento para este primer requestMapping seria el metodo privado makeGameDTO, el mismo , como bien se indica seria
    //el encargado de conformar el objeto de transferencia de datos correspondiente a los parametros del game, teniendose en cuenta
    //el id de ese game, la fecha de iniciacion , asi como los jugadores que interactuan en el mismo(id,"init_date" y players_in_game),
    //vease que para el ultimo parametro players in game en donde se accederia a la informacion de los set de jugadores envueltos en juegos
    //para esa clase , y vease que dicho metodo (getGamePlayerSet)fue previamente creado en la clase Game como getter para acceder a cualesquiera
    //informacion que la clase gameplayer almacenase acerca de sus jugadores envueltos en cada partida.Donde una vez obtenidos estos sets de
    //jugadores por cada partida entonces se procederia a mapearlos , mediante funcion stream, ademas de la funcion sort (para ordenarlos de
    //manera correcta , y por ultimo una funcion lambda que arrojaria ese set de players para ese especifico game dentro del set de gamePlayers
    // en si de ahi que se utilice el previamente inicializado metodo constructor de DTO makeGamePlayerDto(con el respectivo parametro gamePlayer)
    //garantizando el acceso al set.
    //Veae que cada uno de los DTO previamente inicializados se complementan uos a otros pues son mutuos en cuanto propiedades y elemntos en comun ,
    //y solo cuando los tres son efectivamente inicializados , entonces en su conjunto serian efectivos para el request hecho al api/game....

    @RequestMapping(value="/api/game_view/{id}",method = RequestMethod.GET)
    public ResponseEntity <Map<String,Object>> findGamePlayerId(@PathVariable("id") Long id,Authentication authentication){
        Map<String,Object> dto = new LinkedHashMap<>();
        if(authentication==null){
            return new ResponseEntity<>(makeMap("Error","Please authenticate the user!!!"),HttpStatus.UNAUTHORIZED);
        }
        Player player=playerLoggedDetails(authentication);
        if(player==null){
            return new ResponseEntity<>(makeMap("Error","This Player doesn't exist, try again!!!"),HttpStatus.UNAUTHORIZED);
        }
        GamePlayer gamePlayer=gamePlayerRepository.getOne(id);
        if(gamePlayer.equals(null)){
            return new ResponseEntity<>(makeMap("Error","This Player doesnt exist in this game!!!"),HttpStatus.UNAUTHORIZED);
        }
        if(!player.equals(gamePlayer.getPlayer())){
            return new ResponseEntity<>(makeMap("Error","Dont cheat!!!"),HttpStatus.CONFLICT);
        }

        dto.put("game",makeGameDTO(gamePlayer.getGame()));
        dto.put("ship",gamePlayer.getShips().stream().map(ship -> makeShipDto(ship)));
        dto.put("mySalvo",gamePlayer.getSalvos().stream().sorted((turn1,turn2)->turn1.getTurn().compareTo(turn2.getTurn())).map(salvo->makeSalvoDto(salvo)));
        dto.put("status", getGameStatus(gamePlayer));


        if(getOppDetails(gamePlayer)!=null){
            dto.put("oppSalvo",getOppDetails(gamePlayer).getSalvos().stream().sorted((turn1,turn2)->turn1.getTurn().compareTo(turn2.getTurn())).map(salvo->makeSalvoDto(salvo)));
//            dto.put("hit",gamePlayer.getSalvos().stream().sorted((salvo1,salvo2)->salvo1.getTurn().compareTo(salvo2.getTurn())).map(salvo -> makeTurnOnHitsDto(salvo)));//esto se anadio a ultima hora para saber
            dto.put("sunk_ships_opponent", sunkShipDto(gamePlayer.salvos,gamePlayer));
//            dto.put("status", getGameStatus(getOppDetails(gamePlayer), sunkSh,sunkShOpponent));
            dto.put("sunk_ships", sunkShipDto(getOppDetails(gamePlayer).salvos,getOppDetails(gamePlayer)));
        }
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
    //segun directrices del proyecto , el segundo request mapping se haria con la intencion de determinar el gamePlayerId que haria el request
    //a esa path especifico, de ahi que en el syntax del path, se especifique en templates la palabra(id, aunque pudo haberse usado cualquier
    // otra)en donde entonces se procederia a mandar datos a la aplicacion teniendo en cuenta tambien un id ,que en este caso seria el del usuario
    //que para ese momento interactua con l aaplicacion con su respectivo id.Como previemante se explico , ya para este momento la aplicacion estaria
    //cerrada dado los paremtros de seguridad de Spring-Security, y para cada operacion que implique acceder a datos de la misma habria que tener
    //ciertos permisos y demas que en caso de no ser efectivos triggerizarian respuestas por default del Spring-Security, o acceptarian el acceso
    // del solicitante una vez comprobada su veracidad, todo esto mediante las subsequentes Response Entities , encargadas de comprobar y arrojar
    //diferentes respuestas mediante los http.status al servidor, de ahi entonces la inicializacion de este metodo publico con el componente
    //ResponseEntity diciendo de antemano de que va el proceso; actoseguido se establece la estructura del objeto que seria mapeable, componiendose
    //de un string, y un objeto, ademas de que se le llamara al metodo findGamePlayerId.Sus parametros como bien se explico , primero contendrian un
    //elemento corresppndoiente a la seguridad de Spring(en este caso Authentication), y un parametro que vincularia dicho metodo al id, cualesquiera que
    //fuese arrojado mediante la respuesta de URL mandada por servidor, esta anotacion seria @PathVariable, y tomaria el id previamente expuesto en el
    //template del requestMapping y su parametro seria Long id, vease que en el caso de authentication , el mismo proviene del metodo
    // mas adelante creado llamdao'playerLogedDetails', dandonos los detalles del jugador que se autentifica en cuestion.
    //Anteriorment se explico que dadas las posibles respuestas que pudiese arrojar el servidor teniendo en cuenta los Analisis del Spring Security
    //al verificar authentication , se suscitarian diferentes respuestas en dependencia de las condicionanets que se expongan:
    //Vease que primero se especifica que se devolvera cierto error de status UNAUTHORIZED, si previamente no se pasa por el proceso de autentificacion
    //Una vez inicializado el proceso de authentificacion se procederia a acceder a la clase Player(pues se supone sea un player , el que se registra
    //para acceder al juego)y se le assigna el parametro authentification proveniente del metodo mas adelante creado llamado playerLoggedDetails.
    //Entonces con el player ya en la palestra se procede a verificar si el mismo es null o existe par el game en cuestion , donde en caso de que fuese
    //mull, se mostraria otra alerta de seguridad con otra respuesta de UNAUTHORIZED .Otro condicional de seguridad seria entonces verificar si ese jugador
    //una vez verificado presenta acciones en un gamplayer existente , o en caso de que el mismo no exista( se null), otro Alerta como response Entity
    //debera ser mostrada.
    //Por ultimo , un ultimo complemento de seguridad seria comparar el player previamente registrado para acceder a este juego corresponde con el player
    //que se almacena para ese game en cuestion(o sea que si la persona que se autentifica es la misma que ya existe en la partida de ese gamplayer
    // como tal, o si es alguien que esta tratando de usurpar dicha identidad, donde en caso de que se cumpliese lo ultimo , se procederia a inicializar
    // una alerta por conflicto y por consiguiente la negacion de entrada a a la aplicacion.
    //Ya pasado las comprobaciones de seguridad , entonces se procederia a conformar la estructora para el DTO que aarcaria la informacion de
    //este gamePlayer para su especifico id, en donde se requeriria el juego en el que participa dicho player, accediendose al mismo  madiente,
    //previo conformado DTO makeGameDto en donde en su interaccion con el game( en un gamePLayer), se accedria al juego en cuestion para este usuario
    //con su ID.
    //Segundo elemento segun las directrices del  juego en cada partida debe haber una serie de naves (ships)con caracteristicas y demas, de ahi que se
    //inicialize este segundo elemento, accediendose a las mismas mediante gamePLayer(vease que en la clase GamePlayer  se inicializa un set de Ships ,
    // al igual que de salvos , pues por logica , cada jugador en su parte del juego tendria a su disposicion  ships y salvos correspondientes
    //a la partida y el acceso a ellos a traves del gameplayer es posible por la inicializacion ademas en cada una de estas clases (ships y salvo)
    //del metodo addShip y addSalvo respectivamente trayendo de las mismas la informacion necesaria , para que tanto , game , y player puedan acceder
    //a ella sin problemas a traves de sus getters ya tambien transferidos(getShips, getSalvos),para posteriormente mediante la estructura de Dto,
    //(makeShipDTO, yMakeSalVoDto)mas adelante creada en sus respectivos metodos complementarios exponer todos los datos en cuanto ships , salvos y demas
    //como ultimo parametro obtenido del game player se inicializa los salvos del oponente accediendos a ellos mediante el metodo mas adelante inicializado
    //getOppDetails, en donde a groso modo se accderia a este gameplayer juego y se determionaria si existe otro jugador en partida , donde en caso
    // de ser asi se procedria a comparar ambos id  retornandose el id diferent al que en cuestion hace el request en el set de gamePLayers, siendo entonces
    //este ultimo el contenedor de la informacon del opononente, en este caso los salvos que el mismo dispara , accediendose a los mismos a traves del
    //previemanete explicado teniendo como parametro el gamePlayer , pues al igual que en el ship , la clase salvo contiene una extension del
    //gamePlayer y su metodo addSalvo , para establecer la conexion entre dichos salvos y el jugador (player) en el playground comun que gamePlayer es.
    //para ya despues acabar con una respuesta satisfactoria de la entidadd (response entity), retornandose el dto ya conformado, asi como el
    //status OK como respuesta del servidor.

    private Map<String,Object>makeShipDto(Ship ship){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("ship_Type",ship.getTypeShip());
        dto.put("ship_location",ship.getShipPositions());

        return dto;
    }
    //como metodo complementario a este segundo RequestMapping se inicializa este contenedor de datos a traves de objetos DTO llamado
    //makeShipDto, y como bien dice su nombre el mismo seria el encargado de disenar la estructura en la que la informacion acerca de los
    //ships sera expuesto a manera de API para su posterior gestion en la aplicacion.Vease que en la misma se toma como parametro , toda la
    //informacion proveniente de la clase Ship de ahi la mencion de esta clase asi como su extension en los parametros que complementan el metodo
    //y como elementos que conformaran dicha data , estarian el tipo de ship, y su localizacion("ship_Type","ship_location"), en donde la informacion
    //para ambos parametros es accedida directamente desde la clase y su extension (ship), obteniendo a traves de ellas sus respectivos getters.
    //Vease que despues como complemento este metodo  se llamaria en el Request maping correspondinete al api/gameview, utilizando la extension
    //de la clase GamePlayer game Player, pues como la logica explica dichos ships pertenecen a uno de los jugadores que toma parte en el juego
    // y en su interaccion con el mismo('gameplayer')se es necesario crear un metodo que especifique su adicion a la partida de ahi addShip en dicha clase
    //y la declaracion reciproca  de incorporacion al gameplayer en la clase Ship, a traves de su constructor, autoanadiendose mediante (this) en su
    //evocacion a dicho metodo

    private Map<String,Object>makeSalvoDto(Salvo salvo){

        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("turn",salvo.getTurn());
        dto.put("shootingstoPosition",salvo.getShootToPositions());
        dto.put("hitOnTarget",makeTurnOnHitsDto(salvo));

        return dto;
    }
    //como metodo complementario a este segundo RequestMapping se inicializa este contenedor de datos a traves de objetos DTO llamado
    //makeSalvoDto, y como bien dice su nombre el mismo seria el encargado de disenar la estructura en la que la informacion acerca de los
    //salvos  sera expuesto a manera de API para su posterior gestion en la aplicacion.Vease que en la misma se toma como parametro , toda la
    //informacion proveniente de la clase Salvo de ahi la mencion de esta clase asi como su extension en los parametros que complementan el metodo
    //y como elementos que conformaran dicha data , estarian el turno de disparo para el jugador en cuestion asi como las coordenades de disparo
    // , y su localizacion("turn","shootingstoPosition""), en donde la informacionpara ambos parametros es accedida directamente desde la clase y
    // su extension (salvo), obteniendo a traves de ellas sus respectivos getters.
    //Vease que despues como complemento este metodo  se llamaria en el Request maping correspondinete al api/gameview, utilizando la extension
    //de la clase GamePlayer game Player, pues como la logica explica dichos salvos  pertenecen a uno de los jugadores que toma parte en el juego
    // y en su interaccion con el mismo('gameplayer')se es necesario crear un metodo que especifique su adicion a la partida de ahi addSalvo en dicha clase
    //y la declaracion reciproca  de incorporacion al gameplayer en la clase Salvo, a traves de su constructor, autoanadiendose mediante (this) en su
    //evocacion a dicho metodo.
    //Es importante recalcar que este metodo se llama dos veces en este request mapping, pues en su primer llamado se hace refernecia a los salvos
    //disparos que uno de lops jugadores involucrados en la partida realiza , en este caso el que esta autentificado  le corresponderia la
    //opcion mySalvo, y entonces en su perfil se recibirian los salvos del opponente como oopSalvo con la misma restructura de DTO que en mysalvos
    //de ahi que se llame dos veces el mismo DTO en el mismo request

    private Map<String,Object>makeGamePlayerIdDTO(GamePlayer gamePlayer){
        Map<String,Object>dto=new LinkedHashMap<>();
        dto.put("GP_id",gamePlayer.getId());
        dto.put("GP_Gcreate",gamePlayer.getDate());
        return dto;
    }
    //Este metdodo en si no se usa aunque a groso modo simplemente daria el id del gamePLayer y la fecha de creacion

    private Map<Integer,Object>makeTurnOnHitsDto( Salvo salvo){
        Map<Integer,Object>dto=new HashMap<>();
        List<String> myArrayShots=new ArrayList<>();
        for (Ship ship:getOppDetails(salvo.getGamePlayer()).getShips()){
            for(String Location :ship.getShipPositions()){
                if(salvo.getShootToPositions().contains(Location)){
                    myArrayShots.add(Location);
                }
            }
        }
        dto.put(salvo.getTurn(),myArrayShots);
        return dto;
    }
    //Vease que en este primer metodo simplemente se establece la logica para determianr cuales salvos hicieron hit o not
    //en las naves de contrario , para eso se procede a inicializar una variable que almacenaria dichos disparos que hacen hit
    //a manera de lista de string . de ahi la creacion de este List<String> llamdo my arrayShots como new Array List.
    //Acto seguido se procede entonces a desarrollar la logica mediante un doble loop en donde primero se procederia a loopear
    //el metodo getOppnoentDetails, obteniendolos los ships que cada jugador posee en particular, de ahi que se determine la clase
    // Ship con su parametro ship, para despues en un segundo nested loop proceder a declararse otra variable de tipo String llamada
    //location, en donde se accederia a la clase Ship para el oponente , obtendiendose las posiciones del ship del mismo;para entonces
    //posteriormente proceder a comparar los salvos disparados a las posiciones del grid del oponente con las posiciones que tienen
    //sus naves en dicho grid en donde ,mediante condicion contain si alguna de esas posiciones coinciden con los disparos hechos;
    //se adicionarian al array previamente creado en la variable myArrayShoots.
    //Para posteriormente proceder a crear dicho dto en donde primero se estableceria el turno , accediendose mediante la variable salvo
    //iniciada en el constructor del metodo , accediendose al ,al metodo getTurn procedente de dicha clase; y como segundo parametro de
    //dicho dto estaria el array myArrayShots con los salvos efectivos almacenados.

    private List<Set<String>>sunkShipDto(Set<Salvo> salvoSet, GamePlayer gamePlayer){
        List<Set<String>>dto=new LinkedList<>();
        Set<String> sunkShips=new HashSet<>();

        for(Ship ship:getOppDetails(gamePlayer).getShips()){
            Integer shipSize=ship.getShipPositions().size();
            for(Salvo salvo:salvoSet.stream().collect(Collectors.toList())){
                for( String LocationShots:ship.getShipPositions()){
                    if(salvo.getShootToPositions().contains(LocationShots)){
                        shipSize=shipSize-1;
                        if(shipSize==0){
                            sunkShips.add(ship.getTypeShip());
                        }
                    }
                }
            }
        }
        dto.add(sunkShips);
        return dto;
    }
    //en este caso se procede a determinaar el array de naves que el oponente tien hundidas segun los disparos
    //hechos por el adversario
//    private List<Set<String>>sunkShipDtoOpp(Set<Salvo> salvoSet, GamePlayer gamePlayer){
//        List<Set<String>>dto=new LinkedList<>();
//        Set<String> sunkShipsOpp=new HashSet<>();
//
//        for(Ship ship:gamePlayer.getShips()){
//            Integer shipSize=ship.getShipPositions().size();
//            for(Salvo salvo:salvoSet.stream().collect(Collectors.toList())){
//                for( String LocationShots:ship.getShipPositions()){
//                    if(salvo.getShootToPositions().contains(LocationShots)){
//                        shipSize=shipSize-1;
//                        if(shipSize==0){
//                            sunkShipsOpp.add(ship.getTypeShip());
//                        }
//                    }
//                }
//            }
//        }
//        dto.add(sunkShipsOpp);
//        return dto;
//    }
    
    private Map<String,Object> getGameStatus(GamePlayer gamePlayer){
        Map<String,Object>dto=new HashMap<>();

        if(getOppDetails(gamePlayer)==null){
            dto.put("state","Waiting for opponent in game");
        }
        else {
            dto.put("state","Welcome to game,place Ships Please");

            if(getOppDetails(gamePlayer).getShips().size()<gamePlayer.getShips().size()){
                dto.put("state","Welcome to game,Waiting for opponent to place ships");
            }
            else{
            if(gamePlayer.getShips().size()==5 ){//&& getOppDetails(gamePlayer).getShips().size()==5)
                dto.put("state","Ships in Coordenates, Shoot!!!");
            }
            if(gamePlayer.salvos.size()>getOppDetails(gamePlayer).salvos.size()){
                dto.put("state","Enemy hasn't shot, wait for their shots");
            }
            if((sunkShipDto(gamePlayer.salvos,gamePlayer).get(0).size()==5) &&
               (sunkShipDto(getOppDetails(gamePlayer).salvos, getOppDetails(gamePlayer)).get(0).size()!=5 )&&
               (getOppDetails(gamePlayer).salvos.size()==gamePlayer.salvos.size())){

                dto.put("state","Win ");
                if(gamePlayer.getPlayer().getScorePerPlayer(gamePlayer.getGame())==null) {
                    Score newScore = new Score(gamePlayer.getPlayer(), gamePlayer.getGame(), 1.0);
                    scoreRepository.save(newScore);
                }

//                Score newScore=new Score(gamePlayer.getPlayer(),gamePlayer.getGame(),1.0);
//                scoreRepository.save(newScore);
            }

            if((sunkShipDto(getOppDetails(gamePlayer).salvos, getOppDetails(gamePlayer)).get(0).size()==5 )&&
               (sunkShipDto(gamePlayer.salvos,gamePlayer).get(0).size()!=5) &&
               (getOppDetails(gamePlayer).salvos.size()==gamePlayer.salvos.size())){

                dto.put("state","Game Over");
                if(gamePlayer.getPlayer().getScorePerPlayer(gamePlayer.getGame())==null) {
                    Score newScore = new Score(gamePlayer.getPlayer(), gamePlayer.getGame(), 0.0);
                    scoreRepository.save(newScore);
                }
//                Score newScore=new Score(gamePlayer.getPlayer(),gamePlayer.getGame(),0.0);
//                scoreRepository.save(newScore);

            }
            if((sunkShipDto(getOppDetails(gamePlayer).salvos, getOppDetails(gamePlayer)).get(0).size()==5 )&&
                    (sunkShipDto(gamePlayer.salvos, gamePlayer).get(0).size()==5 )&&
                    (getOppDetails(gamePlayer).salvos.size()==gamePlayer.salvos.size())){
                dto.put("state","Draw");
                if(gamePlayer.getPlayer().getScorePerPlayer(gamePlayer.getGame())==null) {
                    Score newScore = new Score(gamePlayer.getPlayer(), gamePlayer.getGame(), 0.5);
                    scoreRepository.save(newScore);
                    Score newScoreOpp = new Score(getOppDetails(gamePlayer).getPlayer(), getOppDetails(gamePlayer).getGame(), 0.5);
                    scoreRepository.save(newScoreOpp);
                }
            }
        }}

        return dto;
    }


//    private Map<String,Object>makeSinkShipDto(GamePlayer gamePlayer){
//        Map<String,Object>dto=new HashMap<>();
//        for(Salvo salvo:gamePlayer.getShips())
//    }////esto para establecer l for que determinan

    //    private Map<Integer,Object>makeSinkShipsDto(GamePlayer gamePlayer){
//        Map<Integer,Object>dto=new HashMap<>();
//        List<String>mySunkShips=new ArrayList<>();
//        for ()
//
//
//        return dto;
//    }
    //Es
    @RequestMapping("/api/game/playerScore")
    public List<Map<String,Object>> getScoresPlayer() {
        return playerRepository.findAll().stream().map(player->getPlayazScore(player)).collect(Collectors.toList());
    }

    @RequestMapping(value="/api/playerRegistering",method=RequestMethod.POST)
    public ResponseEntity <Map<String,Object>> registerUser(@RequestBody Player player) {

        if (player.getUserName().isEmpty() || player.getPassword().isEmpty() || player.getEmail().isEmpty()) {
            return new ResponseEntity<>( makeMap("Error"," please fill all fields"), HttpStatus.FORBIDDEN);
        }
//        Player player = playerRepository.findByUserName(userName);

        if (playerRepository.findByUserName(player.getUserName()) != null) {
            return new ResponseEntity<>(
                    makeMap("Error"," please try with other one"), HttpStatus.FORBIDDEN);
        }
        player.setPassword( passwordEncoder.encode(player.getPassword()));
        Player newPlayer = playerRepository.save(player);
        return new ResponseEntity<>(makeMap("id", newPlayer.getId()), HttpStatus.CREATED);
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
    @RequestMapping(value="/api/game",method=RequestMethod.POST)
    public ResponseEntity <Map<String,Object>> createGame(Authentication authentication) {
        if(authentication==null){
            return new ResponseEntity<>(
                    makeMap("Error","you should be looged in!"), HttpStatus.FORBIDDEN);
        }
        Player player = playerLoggedDetails(authentication);

        if (player == null) {
            return new ResponseEntity<>(
                    makeMapCreateGame("Error"," please try with other one"), HttpStatus.FORBIDDEN);
        }


        Game newGame  = new Game(new Date());
        gameRepository.save(newGame);
        GamePlayer gamePlayer = new GamePlayer(player, newGame, new Date());
        gamePlayerRepository.save(gamePlayer);
        return new ResponseEntity<>(makeMapCreateGame("gpid", gamePlayer.getId()), HttpStatus.CREATED);
    }

    private Map<String, Object> makeMapCreateGame(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
    @RequestMapping(value="/api/game/{gamePlayerId}/ship",method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>>getListOfShips(Authentication authentication,@PathVariable("gamePlayerId")Long id,@RequestBody Set<Ship> ships){
        if(authentication==null){
            return new ResponseEntity<>(makeMap("Error","You Can Not Join To this Game"),HttpStatus.FORBIDDEN);
        }
        Player player=playerLoggedDetails(authentication);
        if(player==null){
            return new ResponseEntity<>(makeMap("Error","You Can Not Join To this Game"),HttpStatus.FORBIDDEN);
        }
        GamePlayer gamePlayer=gamePlayerRepository.getOne(id);
        if(gamePlayer==null){
            return new ResponseEntity<>(makeMap("Error","You Can Not Join To this Game, cause doesn't exist"),HttpStatus.UNAUTHORIZED);
        }
        for(Ship ship:ships){
            if(ship.getShipPositions().size()==0){
                return new ResponseEntity<>(makeMap("Error","You Can't send ships withput position"),HttpStatus.FORBIDDEN);
            }
        }

        if(gamePlayer.ships.size() == 5){
            return new ResponseEntity<>(makeMap("Error","You Can Not Place Ships, Already Placed"),HttpStatus.FORBIDDEN);
        }
//        if(gamePlayer.ships.size() <4){
//            return new ResponseEntity<>(makeMap("Error","You Can Place More Ships!!!"),HttpStatus.FORBIDDEN);
//        }

        ships.stream().forEach(ship -> {
            gamePlayer.addShip(ship);
            shipReposotiry.save(ship);
        });

        return new ResponseEntity<>(makeMap("placedships","Placed "), HttpStatus.CREATED);
    }

    @RequestMapping(value="/api/game/{gamePlayerId}/salvoes",method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>>getListOfShotSalvos(Authentication authentication,@PathVariable("gamePlayerId")Long id, @RequestBody List<String> salvoLocations){
        if(authentication==null){
            return new ResponseEntity<>(makeMap("Error","You Can Not Join To this Game"),HttpStatus.FORBIDDEN);
        }
        Player player=playerLoggedDetails(authentication);
        if(player==null){
            return new ResponseEntity<>(makeMap("Error","You Can Not Join To this Game"),HttpStatus.FORBIDDEN);
        }
        GamePlayer gamePlayer=gamePlayerRepository.getOne(id);

        if(gamePlayer==null){
            return new ResponseEntity<>(makeMap("Error","You Can Not Join To this Game, cause doesn't exist"),HttpStatus.UNAUTHORIZED);
        }
        if(gamePlayer.ships.size()<5){
            return new ResponseEntity<>(makeMap("Error","You Can Not Shoot until ships are placed"),HttpStatus.FORBIDDEN);
        }
        if(getOppDetails(gamePlayer).ships.size()<5){
            return new ResponseEntity<>(makeMap("Error","You Can Not Shoot untill ships of oponent are placed are placed"),HttpStatus.FORBIDDEN);
        }
        if(salvoLocations.size() > 5){
            return new ResponseEntity<>(makeMap("Error","You Can Not Place more than 5 shots"),HttpStatus.FORBIDDEN);
        }
        if(salvoLocations.size() < 5){
            return new ResponseEntity<>(makeMap("Error","You have to add more Ammunition,Shoot!!!"),HttpStatus.FORBIDDEN);
        }
        if(getOppDetails(gamePlayer).salvos.size() < gamePlayer.salvos.size()){
            return new ResponseEntity<>(makeMap("Error","You can't shoot, Wait for your second turn !!!"),HttpStatus.FORBIDDEN);
        }

        Salvo salvo = new Salvo(gamePlayer.salvos.size()+1,salvoLocations,gamePlayer);

        salvoRepository.save(salvo);
        return new ResponseEntity<>(makeMap("placedshots","Placed "), HttpStatus.CREATED);
    }
    @RequestMapping(value="api/game/{game_id}",method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>>joinToGame (Authentication authentication,@PathVariable("game_id") Long id){
        if(authentication==null){
            return new ResponseEntity<>(makeMapJoinGame("Error","You Can Not Join To this Game"),HttpStatus.FORBIDDEN);
        }
        Player player=playerLoggedDetails(authentication);
        if(player==null){
            return new ResponseEntity<>(makeMapJoinGame("Error","You Can Not Join To this Game"),HttpStatus.FORBIDDEN);
        }
        Game game = gameRepository.findAllById(id);
        if(game==null){
            return new ResponseEntity<>(makeMapJoinGame("Error","You Can Not Join To this Game, cause doesnt exist"),HttpStatus.FORBIDDEN);
        }
        if(game.getGamePlayerSet().size()!=1){
            return new ResponseEntity<>(makeMapJoinGame("Error","You Can Not Join To this Game, cause is full"),HttpStatus.FORBIDDEN);
        }
        GamePlayer gamePlayer =new GamePlayer(player,game,new Date());
        gamePlayerRepository.save(gamePlayer);
        return new ResponseEntity<>(makeMapJoinGame("gpid", gamePlayer.getId()), HttpStatus.CREATED);
    }
    private Map<String, Object> makeMapJoinGame(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }


//    @RequestMapping(value="/api/playerLoged/details",method=RequestMethod.GET)
//    public Player getDetUserLogged(Authentication authentication){
//        return playerRepository.findByUserName(authentication.getName());
//    }




    //////////////////////////////////COMMON METHODS////////////////////////////////////////////////////
    private Map<String,Object> getPlayazScore( Player player){
        Map<String,Object> dto =new LinkedHashMap<>();
        dto.put("player",makePlayerDTO(player));
        dto.put("playerScore",player.getScores());
        return dto;
    }
    private GamePlayer getOppDetails(GamePlayer gamePlayer){
        return gamePlayer.getGame().getGamePlayerSet()
                .stream()
                .filter(gamePlayer1 -> !gamePlayer1.getId().equals(gamePlayer.getId()))
                .findFirst().orElse(null);

//        for (GamePlayer gamePlayer1:gamePlayer.getGame().getGamePlayerSet()) {
//            if (gamePlayer.getId()!= gamePlayer1.getId()){
//                return gamePlayer1;
//            }
//        }
//        return  null;
    }
    private Player playerLoggedDetails(Authentication authentication){
        return playerRepository.findByUserName(authentication.getName());


    }

}
