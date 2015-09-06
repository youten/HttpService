package youten.redo.httpservice;

import youten.redo.httpservice.event.HEvent;

public interface HttpEventListener {
    public void onEvent(HEvent event);
}
