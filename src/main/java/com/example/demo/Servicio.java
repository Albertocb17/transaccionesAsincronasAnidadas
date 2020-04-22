package com.example.demo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@Transactional
public class Servicio {
	
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private ServicioAuditoria auditoria;
	
	@Autowired
	private ServicioAsincrono asincrono;
	
//	@TransactionalEventListener
	public Persona alta (String nombre) throws Exception {
		
		auditoria.log("alta de " + nombre);
		
		Persona persona = new Persona();
		persona.setNombre(nombre);
		
		em.persist(persona);
		
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter(){
            public void beforeCompletion() {
            	try {
					asincrono.cambiarNombreAsincronoTransaccional(persona);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                //do stuff right after commit
                System.out.println("commit!!!");

            }
        });
		
//		asincrono.cambiarNombreAsincronoTransaccional(persona);
		
//		CompletableFuture.runAsync(() -> cambiarNombreAsincronoTransaccional(persona));
		
//		CompletableFuture<Persona> completableFuture = new CompletableFuture<>();
//		cambiarNombreAsincrono3(completableFuture);
//		completableFuture.complete(persona);
//		
//		CompletableFuture.runAsync(() -> cambiarNombreAsincrono2(persona.getId()));
		
//		CompletableFuture.supplyAsync(() -> cambiarNombreAsincrono(persona.getId()))
//			.thenAccept(value -> em.merge(value));
		
		return persona;
	}
	
//	@Async
//	private Persona cambiarNombreAsincrono(long personaId) {
//
//		Persona personaPersistida = em.find(Persona.class, personaId);
//
//		// Ejecuto algo que tarde mucho
//		pausa();
//
//		String nombre = personaPersistida.getNombre();
//		personaPersistida.setNombre("Modificado");
//
//		auditoria.log("Modificando nombre de persona " + nombre + " a " + personaPersistida.getNombre());
//
//		em.merge(personaPersistida);
//
//		return personaPersistida;
//	}
//	
//	@Async
//	private void cambiarNombreAsincrono2(long personaId) {
//
//		Persona personaPersistida = em.find(Persona.class, personaId);
//
//		// Ejecuto algo que tarde mucho
//		pausa();
//
//		String nombre = personaPersistida.getNombre();
//		personaPersistida.setNombre("Modificado");
//
//		auditoria.log("Modificando nombre de persona " + nombre + " a " + personaPersistida.getNombre());
//
//		em.merge(personaPersistida);
//
//	}
//	
//	private void cambiarNombreAsincrono3(CompletableFuture<Persona> future) {
//		future.thenAccept(persona -> {
//			// Ejecuto algo que tarde mucho
//			pausa();
//			String nombre = persona.getNombre();
//			persona.setNombre("Modificado");
//
//			auditoria.log("Modificando nombre de persona " + nombre + " a " + persona.getNombre());
//		});
//	}

//	private void pausa() {
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
	
	public Persona consulta (long id) {
		
		Persona existente = em.find(Persona.class, id);
		
		auditoria.log(existente);
		
		if (existente.getConsultas() > 3) {
			throw new RuntimeException("demasiadas consultas");
		}
		
		return existente;
	}


}
