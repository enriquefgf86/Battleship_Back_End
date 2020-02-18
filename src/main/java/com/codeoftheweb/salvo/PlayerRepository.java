package com.codeoftheweb.salvo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player, String> {

 Player findByUserName(String userName);//,String password);

}
//vease que para cada una de las clases creadas se debe inicializar un repositorio de JPA, facilitador por excelencia para enviar
//instancias de cada una de las clases del proyecto a browsers y otros web search clients a manera de notacion JSON.vease que siempre
//se inicializa como un public interface y como prefijo se le pone el nombre d ela clase la cual se le hace el repo seguido de repository
//o sea PLayer-Repository, y se hace alusion a que la misma se extiende a la libreria de JPA repositories, especificandosele como parametros
//el constructor de la clase en cuestion asi como el parametro String en este caso pues lo que se obtendria de la clase player seria un String
//que nos daria el userName, a diferencia de la clase Game dentro de este metodo de repositorio si se debe especificar que es lo que se enviaria
//pues el anterior al no tener metodo alguno declarado al no ser el de fecha de creacion no demandaba algo mas, pero en este caso se debe
//especificar que es lo que se mandara como notacion JSON de este repositorio, de ahi que se determine sobre que se debe buscar en este
//repositorio en este caso sobre una lista de parametros arrojados por la clase Player especificandosele que se emitira dicho Json repository
//teniendo en cuenta los elementos String userName