package com.codeoftheweb.salvo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface GamePlayerRepository extends JpaRepository<GamePlayer,Long> {

}
//en el caso del Game player el concepto gemeral seria el mismo , pero al ser Game player receptor o punto en comun de
//ambas clases , no requiere parametro adicional como lo tuvo Player o lo tendria Game e caso de qeu aparte del Date se le inicializara
//otro parametro

