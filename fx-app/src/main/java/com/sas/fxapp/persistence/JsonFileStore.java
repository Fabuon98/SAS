package com.sas.fxapp.persistence;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Utility per leggere e scrivere liste di oggetti su file JSON.
 */
public class JsonFileStore {
    // Cartella di base in cui troveranno i file JSON
    private final Path basePath;

    // Mapper di Jackson configurato per indentare l'output e supportare i moduli (es. JavaTime)
    private final ObjectMapper mapper = new ObjectMapper()
        .findAndRegisterModules()            // registra moduli come JavaTime
        .enable(SerializationFeature.INDENT_OUTPUT); // output formattato

    /**
     * Costruttore: imposta la directory di lavoro.
     * @param basePath percorso della cartella di destinazione/lettura
     */
    public JsonFileStore(Path basePath) {
        this.basePath = basePath;
    }

    /**
     * Legge tutti gli oggetti di tipo T contenuti nel file JSON.
     * @param fileName nome del file (es. "eventi.json")
     * @param type     classe dell'array T[] per la deserializzazione
     * @param <T>      tipo degli oggetti
     * @return lista di oggetti letti, vuota se il file non esiste
     */
    public <T> List<T> readAll(String fileName, Class<T[]> type) {
        try {
            // costruiamo il percorso al file
            Path p = basePath.resolve(fileName);
            // se non esiste, ritorniamo lista vuota
            if (!Files.exists(p)) return new ArrayList<>();
            // deserializziamo l'array T[] dal file
            T[] arr = mapper.readValue(p.toFile(), type);
            // e lo trasformiamo in List<T>
            return new ArrayList<>(Arrays.asList(arr));
        } catch (IOException e) {
            // in caso di errore I/O, rilanciamo unchecked
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Serializza e scrive su file JSON la lista di oggetti.
     * @param fileName nome del file (es. "eventi.json")
     * @param items    lista di oggetti da salvare
     * @param <T>      tipo degli oggetti
     */
    public <T> void writeAll(String fileName, List<T> items) {
        try {
            // percorso completo al file
            Path p = basePath.resolve(fileName);
            // assicuriamoci che le cartelle esistano
            Files.createDirectories(p.getParent());
            // scriviamo la lista in JSON, sostituendo il file
            mapper.writeValue(p.toFile(), items);
        } catch (IOException e) {
            // errore di scrittura rilanciato come unchecked per non dover dichiarare throws
            throw new UncheckedIOException(e);
        }
    }
}
