package com.sas.fxapp.persistence;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.sas.fxapp.model.EventoDTO;
import com.sas.fxapp.model.MenuDTO;
import com.sas.fxapp.model.PreparazioneDTO;
import com.sas.fxapp.model.RicettaDTO;
import com.sas.fxapp.model.SezioneDTO;

public class DataService {
    private final JsonFileStore store =
        new JsonFileStore(Paths.get("src/main/resources/data"));

    // ===== EVENTI =====

    public List<EventoDTO> findAllEventi() {
        return store.readAll("eventi.json", EventoDTO[].class);
    }

    public Optional<EventoDTO> findEventoById(Long id) {
        for (EventoDTO e : findAllEventi()) {
            if (Objects.equals(e.getId(), id)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    public Long nextEventoId() {
        long max = 0L;
        for (EventoDTO e : findAllEventi()) {
            if (e.getId() != null && e.getId() > max) {
                max = e.getId();
            }
        }
        return max + 1;
    }

    public void saveEvento(EventoDTO e) {
        List<EventoDTO> all = findAllEventi();
        if (e.getId() == null) {
            e.setId(nextEventoId());
        }
        List<EventoDTO> updated = new ArrayList<>();
        for (EventoDTO x : all) {
            if (!Objects.equals(x.getId(), e.getId())) {
                updated.add(x);
            }
        }
        updated.add(e);
        store.writeAll("eventi.json", updated);
    }

    public void deleteEvento(Long id) {
        List<EventoDTO> all = findAllEventi();
        List<EventoDTO> updated = new ArrayList<>();
        for (EventoDTO x : all) {
            if (!Objects.equals(x.getId(), id)) {
                updated.add(x);
            }
        }
        store.writeAll("eventi.json", updated);
    }

    // ===== MENU =====

    public List<MenuDTO> findAllMenu() {
        return store.readAll("menu.json", MenuDTO[].class);
    }

    public Optional<MenuDTO> findMenuById(Long id) {
        for (MenuDTO m : findAllMenu()) {
            if (Objects.equals(m.getId(), id)) {
                return Optional.of(m);
            }
        }
        return Optional.empty();
    }

    public Long nextMenuId() {
        long max = 0L;
        for (MenuDTO m : findAllMenu()) {
            if (m.getId() != null && m.getId() > max) {
                max = m.getId();
            }
        }
        return max + 1;
    }

    public void saveMenu(MenuDTO m) {
        List<MenuDTO> all = findAllMenu();
        if (m.getId() == null) {
            m.setId(nextMenuId());
        }
        List<MenuDTO> updated = new ArrayList<>();
        for (MenuDTO x : all) {
            if (!Objects.equals(x.getId(), m.getId())) {
                updated.add(x);
            }
        }
        updated.add(m);
        store.writeAll("menu.json", updated);
    }

    public void deleteMenu(Long id) {
        List<MenuDTO> all = findAllMenu();
        List<MenuDTO> updated = new ArrayList<>();
        for (MenuDTO x : all) {
            if (!Objects.equals(x.getId(), id)) {
                updated.add(x);
            }
        }
        store.writeAll("menu.json", updated);
    }

    // ===== SEZIONI =====

    public List<SezioneDTO> findAllSezioni() {
        return store.readAll("sezioni.json", SezioneDTO[].class);
    }

    public Optional<SezioneDTO> findSezioneById(Long id) {
        for (SezioneDTO s : findAllSezioni()) {
            if (Objects.equals(s.getId(), id)) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }

    public Long nextSezioneId() {
        long max = 0L;
        for (SezioneDTO s : findAllSezioni()) {
            if (s.getId() != null && s.getId() > max) {
                max = s.getId();
            }
        }
        return max + 1;
    }

    public void saveSezione(SezioneDTO s) {
        List<SezioneDTO> all = findAllSezioni();
        if (s.getId() == null) {
            s.setId(nextSezioneId());
        }
        List<SezioneDTO> updated = new ArrayList<>();
        for (SezioneDTO x : all) {
            if (!Objects.equals(x.getId(), s.getId())) {
                updated.add(x);
            }
        }
        updated.add(s);
        store.writeAll("sezioni.json", updated);
    }

    public void deleteSezione(Long id) {
        List<SezioneDTO> all = findAllSezioni();
        List<SezioneDTO> updated = new ArrayList<>();
        for (SezioneDTO x : all) {
            if (!Objects.equals(x.getId(), id)) {
                updated.add(x);
            }
        }
        store.writeAll("sezioni.json", updated);
    }

    // ===== RICETTE =====

    public List<RicettaDTO> findAllRicette() {
        return store.readAll("ricette.json", RicettaDTO[].class);
    }

    public Optional<RicettaDTO> findRicettaById(Long id) {
        for (RicettaDTO r : findAllRicette()) {
            if (Objects.equals(r.getId(), id)) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    public Long nextRicettaId() {
        long max = 0L;
        for (RicettaDTO r : findAllRicette()) {
            if (r.getId() != null && r.getId() > max) {
                max = r.getId();
            }
        }
        return max + 1;
    }

    public void saveRicetta(RicettaDTO r) {
        List<RicettaDTO> all = findAllRicette();
        if (r.getId() == null) {
            r.setId(nextRicettaId());
        }
        List<RicettaDTO> updated = new ArrayList<>();
        for (RicettaDTO x : all) {
            if (!Objects.equals(x.getId(), r.getId())) {
                updated.add(x);
            }
        }
        updated.add(r);
        store.writeAll("ricette.json", updated);
    }

    public void deleteRicetta(Long id) {
        List<RicettaDTO> all = findAllRicette();
        List<RicettaDTO> updated = new ArrayList<>();
        for (RicettaDTO x : all) {
            if (!Objects.equals(x.getId(), id)) {
                updated.add(x);
            }
        }
        store.writeAll("ricette.json", updated);
    }

    // ===== PREPARAZIONI =====

    public List<PreparazioneDTO> findAllPreparazioni() {
        return store.readAll("preparazioni.json", PreparazioneDTO[].class);
    }

    public Optional<PreparazioneDTO> findPreparazioneById(Long id) {
        for (PreparazioneDTO p : findAllPreparazioni()) {
            if (Objects.equals(p.getId(), id)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    public Long nextPreparazioneId() {
        long max = 0L;
        for (PreparazioneDTO p : findAllPreparazioni()) {
            if (p.getId() != null && p.getId() > max) {
                max = p.getId();
            }
        }
        return max + 1;
    }

    public void savePreparazione(PreparazioneDTO p) {
        List<PreparazioneDTO> all = findAllPreparazioni();
        if (p.getId() == null) {
            p.setId(nextPreparazioneId());
        }
        List<PreparazioneDTO> updated = new ArrayList<>();
        for (PreparazioneDTO x : all) {
            if (!Objects.equals(x.getId(), p.getId())) {
                updated.add(x);
            }
        }
        updated.add(p);
        store.writeAll("preparazioni.json", updated);
    }

    public void deletePreparazione(Long id) {
        List<PreparazioneDTO> all = findAllPreparazioni();
        List<PreparazioneDTO> updated = new ArrayList<>();
        for (PreparazioneDTO x : all) {
            if (!Objects.equals(x.getId(), id)) {
                updated.add(x);
            }
        }
        store.writeAll("preparazioni.json", updated);
    }
}
