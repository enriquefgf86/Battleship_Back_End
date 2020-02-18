package com.codeoftheweb.salvo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;
import java.util.List;
@RepositoryRestResource
public interface GameRepository extends JpaRepository<Game, Long> {
    Game findAllById(Long id);
}
//vease que para cada una de las clases creadas se debe inicializar un repositorio de JPA, facilitador por excelencia para enviar
//instancias de cada una de las clases del proyecto a browsers y otros web search clients a manera de notacion JSON.vease que siempre
//se inicializa como un public interface y como prefijo se le pone el nombre d ela clase la cual se le hace el repo seguido de repository
//o sea Game-Repository, y sehece alusion a que la misma se extiende a l alibreria de JPA repositories, especificandosele como parametros
//el constructor de la clase en cuestion asi como el paramtro long(o OBject)