package org.anusikh;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmProvider;

public class RegisterEventListener implements EventListenerProvider {

    private final KeycloakSession session;
    private final RealmProvider realmProvider;

    public RegisterEventListener(KeycloakSession keycloakSession) {
        this.session = keycloakSession;
        this.realmProvider = keycloakSession.realms();
    }

    @Override
    public void onEvent(Event event) {
        if (EventType.REGISTER.equals(event.getType())) {
            System.out.println("register callback here");
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {

    }

    @Override
    public void close() {

    }
}
