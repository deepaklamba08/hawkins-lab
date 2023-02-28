package org.stranger.data.store.repo;

import org.stranger.common.exception.StrangerExceptions;
import org.stranger.common.model.application.Application;
import org.stranger.common.model.id.Id;

public interface ApplicationRepository {
    public Application lookupApplication(Id applicationId) throws StrangerExceptions.ObjectNotFoundException;

}
