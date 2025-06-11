// EventController.java - Gestione degli eventi
package com.catering.controller;

import com.catering.model.Event;
import com.catering.model.Menu;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventController {
    private List<Event> events;
    private int nextEventId = 1;
    
    public EventController() {
        this.events = new ArrayList<>();
    }
    
    // ========== GESTIONE EVENTI ==========
    public Event createEvent(String name, LocalDate date, String location, int numberOfGuests) {
        Event event = new Event(name, date, location, numberOfGuests);
        event.setId(nextEventId++);
        events.add(event);
        return event;
    }
    
    public List<Event> getAllEvents() {
        return new ArrayList<>(events);
    }
    
    public Event getEventById(int id) {
        return events.stream()
                .filter(event -> event.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    public boolean updateEvent(Event event) {
        Event existingEvent = getEventById(event.getId());
        if (existingEvent != null) {
            existingEvent.setName(event.getName());
            existingEvent.setDate(event.getDate());
            existingEvent.setLocation(event.getLocation());
            existingEvent.setNumberOfGuests(event.getNumberOfGuests());
            return true;
        }
        return false;
    }
    
    public boolean deleteEvent(int id) {
        return events.removeIf(event -> event.getId() == id);
    }
    
    // ========== GESTIONE MENU NEGLI EVENTI ==========
    public void addMenuToEvent(int eventId, Menu menu) {
        Event event = getEventById(eventId);
        if (event != null && menu != null) {
            event.addMenu(menu);
        }
    }
    
    public void removeMenuFromEvent(int eventId, int menuId) {
        Event event = getEventById(eventId);
        if (event != null) {
            event.getMenus().removeIf(menu -> menu.getId() == menuId);
        }
    }
    
    public List<Menu> getMenusForEvent(int eventId) {
        Event event = getEventById(eventId);
        return event != null ? event.getMenus() : new ArrayList<>();
    }
}