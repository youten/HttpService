package youten.redo.httpservice;

import youten.redo.httpservice.event.HEvent;

interface HttpServiceListener {
    boolean onEvent(in HEvent event);
}
