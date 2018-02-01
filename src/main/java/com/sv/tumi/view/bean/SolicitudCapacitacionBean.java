package com.sv.tumi.view.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import com.sv.tumi.db.dao.AreaDAO;
import com.sv.tumi.db.dao.CargoDAO;
import com.sv.tumi.db.dao.CursoCapacitacionDAO;
import com.sv.tumi.db.dao.CursoDAO;
import com.sv.tumi.db.dao.CursoEvaluacionDAO;
import com.sv.tumi.db.dao.CursoNivelDAO;
import com.sv.tumi.db.dao.EspecialidadDAO;
import com.sv.tumi.db.dao.EstadoDAO;
import com.sv.tumi.db.dao.EvaluacionDAO;
import com.sv.tumi.db.dao.NivelDAO;
import com.sv.tumi.db.dao.PersonalCapacitacionDAO;
import com.sv.tumi.db.dao.PersonalDAO;
import com.sv.tumi.db.dao.PreguntasDAO;
import com.sv.tumi.db.dao.SolicitudCapacitacionDAO;
import com.sv.tumi.db.dao.SubtemaDAO;
import com.sv.tumi.db.dao.TemaDAO;
import com.sv.tumi.db.dao.TipoevaluacionDAO;
import com.sv.tumi.db.entity.Area;
import com.sv.tumi.db.entity.Cargo;
import com.sv.tumi.db.entity.Curso;
import com.sv.tumi.db.entity.Cursoevaluacion;
import com.sv.tumi.db.entity.Cursoevaluacionpregunta;
import com.sv.tumi.db.entity.Cursonivel;
import com.sv.tumi.db.entity.Especialidad;
import com.sv.tumi.db.entity.Estado;
import com.sv.tumi.db.entity.Evaluacion;
import com.sv.tumi.db.entity.Nivel;
import com.sv.tumi.db.entity.Personal;
import com.sv.tumi.db.entity.PersonalCapacitacion;
import com.sv.tumi.db.entity.Pregunta;
import com.sv.tumi.db.entity.Solicitudcapacitacion;
import com.sv.tumi.db.entity.Subtema;
import com.sv.tumi.db.entity.Tema;
import com.sv.tumi.db.entity.Tipoevaluacion;
import com.sv.tumi.util.PersonalLogeado;
import com.sv.tumi.util.TumiPropertyValues;
import com.sv.tumi.view.CursoView;
import com.sv.tumi.view.SubtemaView;
import com.sv.tumi.view.TemaView;

@ManagedBean
@SessionScoped
public class SolicitudCapacitacionBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Area area;
	private Solicitudcapacitacion solicitudcapacitacion;
	private Integer areaCod;
	private Integer cargoCod;
	private Solicitudcapacitacion selectedSolicitud;
	
	//campos de formulario
	private Integer codigo;
	private Date fechaInicio;
	private Date fechaFin;
	private Date fechaRegistro;
	private Integer estado;
	
	private String cargo;
	private Map<String,String> areas;
    private Map<String,String> cargos;
    Map<String, Object> filter = new HashMap<String, Object>();
    private List<Area> areasList;
    private List<Cargo> cargosList;
    private List<Personal> personal;
    private List<Solicitudcapacitacion> solicitudes;
    private List<Especialidad> especialidades;
    private Especialidad especialidadSelected;
    private List<Curso> cursos;
    private Curso cursoSelected;
    private List<Tema> temas;
    private List<Tema> temasSelected;
    private List<Subtema> subtemas;
    private List<Subtema> subtemasSelected;
    private List<CursoView> cursosView = new ArrayList<CursoView>();
    private Integer nivelSelected;
    private CursoView cursoEliminar;
    private Date evaluacionFechaInicio;
    private Date evaluacionFechaFin;
    private String comentarioRechazo;
    private Date maxFechaInicio;
    
    private List<String> selectedTest = new ArrayList<String>();
    private List<String> selectedTest2 = new ArrayList<String>();
	
    private EspecialidadDAO especialidadDAO = new EspecialidadDAO();
    private CursoDAO cursoDAO  = new CursoDAO();
    private TemaDAO temaDAO  = new TemaDAO();
    private NivelDAO nivelDAO  = new NivelDAO();
    private SubtemaDAO subtemaDAO  = new SubtemaDAO();
    private AreaDAO areaDAO = new AreaDAO();
	private CargoDAO cargoDAO = new CargoDAO();
	private PersonalDAO personalDAO = new PersonalDAO();
	private SolicitudCapacitacionDAO solicitudCapacitacionDAO = new SolicitudCapacitacionDAO();
	private PersonalCapacitacionDAO personalCapacitacionDAO = new PersonalCapacitacionDAO();
	private EstadoDAO estadoDAO = new EstadoDAO();
	private CursoNivelDAO cursoNivelDAO = new CursoNivelDAO();
	private CursoEvaluacionDAO cursoEvaluacionDAO = new CursoEvaluacionDAO();
	private CursoCapacitacionDAO cursoCapacitacionDAO = new CursoCapacitacionDAO();
	private TipoevaluacionDAO tipoevaluacionDAO = new TipoevaluacionDAO();
	private EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
	private PreguntasDAO preguntasDAO = new PreguntasDAO();
	
	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Date today = new Date();

	
	private DualListModel<Personal> personalList;
	
	@PostConstruct
	public void inicializar() {
		filter.clear();
		
		Calendar now = Calendar.getInstance();
		now.add(Calendar.YEAR, 1);
		now.add(Calendar.MONTH, 6);
		
		maxFechaInicio =now.getTime();
		
		solicitudes = solicitudCapacitacionDAO.findByProperty(filter);
	}
	
	public void irRegistrar() throws IOException, ParseException{
		areaCod=null;
		cargoCod=null;
		areas = new HashMap<String, String>();
		personalList = new DualListModel<Personal>(new ArrayList<Personal>(), new ArrayList<Personal>());
		
		areasList = new ArrayList<Area>();
		filter.clear();
		areasList = areaDAO.findByProperty(filter);
		
		for (Area area : areasList) {
			areas.put(area.getCodigo().toString(), area.getNombre());
		}
		
		solicitudcapacitacion = new Solicitudcapacitacion();
		
		
		//
		
		Date todayWithZeroTime = formatter.parse(formatter.format(today));
		
		//
		solicitudcapacitacion.setFechaRegistro(todayWithZeroTime);
		
		FacesContext.getCurrentInstance().getExternalContext()
		.redirect("/sistema-capacitaciones-tumi/app/solicitudCapacitacion/registrar.xhtml");
	}
	
	public void buscar() throws IOException{
		filter.clear();
		
		if(codigo!=null && codigo!=0){
			filter.put("codigo", codigo);
		}
		
		if(fechaInicio!=null){
			filter.put("fechaInicio", fechaInicio);
		}
		
		if(fechaFin!=null){
			filter.put("fechaFin", fechaFin);
		}
		
		if(fechaRegistro!=null){
			filter.put("fechaRegistro", fechaRegistro);
		}
		
		if(estado!=null && estado!=0){
			filter.put("codigoEstado.codigo", estado);
		}
		
		solicitudes = solicitudCapacitacionDAO.findByProperty(filter);
		
		codigo=null;
		fechaInicio=null;
		fechaFin=null;
		fechaRegistro=null;
		estado=null;
		
		FacesContext.getCurrentInstance().getExternalContext()
		.redirect("/sistema-capacitaciones-tumi/app/solicitudCapacitacion/consultar.xhtml");
	}
	
	public void buscarRegistrados() throws IOException{
		filter.clear();
		
		filter.put("codigoEstado.codigo", 3);
		
		if(codigo!=null && codigo!=0){
			filter.put("codigo", codigo);
		}
		
		if(fechaInicio!=null){
			filter.put("fechaInicio", fechaInicio);
		}
		
		if(fechaFin!=null){
			filter.put("fechaFin", fechaFin);
		}
		
		if(fechaRegistro!=null){
			filter.put("fechaRegistro", fechaRegistro);
		}
		
		solicitudes = solicitudCapacitacionDAO.findByProperty(filter);
		
		codigo=null;
		fechaInicio=null;
		fechaFin=null;
		fechaRegistro=null;
		
		FacesContext.getCurrentInstance().getExternalContext()
		.redirect("/sistema-capacitaciones-tumi/app/solicitudCapacitacion/consultarRegistrados.xhtml");
	}
	
	public void eliminarCurso() throws IOException{
		
		cursosView.remove(cursoEliminar);
		
	}
	
	public void actualizar() throws IOException{
		
		List<PersonalCapacitacion> personalCapacitacionList = new ArrayList<PersonalCapacitacion>();
		
		for (PersonalCapacitacion personalCapacitacion : solicitudcapacitacion.getPersonalCapacitacion()) {
			personalCapacitacionDAO.remove(personalCapacitacion.getCodigo());
		}
		
		for (Personal personal : personalList.getTarget()) {
			PersonalCapacitacion personalCapacitacion = new PersonalCapacitacion();
			personalCapacitacion.setCodigoPersonal(personal);
			personalCapacitacion.setCodigoSolicitudCapacitacion(solicitudcapacitacion);
			personalCapacitacionDAO.create(personalCapacitacion);
		}
		
		solicitudcapacitacion.setPersonalCapacitacion(personalCapacitacionList);
		solicitudCapacitacionDAO.edit(solicitudcapacitacion);
		buscar();
	}
	
	public void rechazar() throws IOException{
		Estado rechazado = estadoDAO.find(2);
		solicitudcapacitacion.setCodigoEstado(rechazado);
		solicitudCapacitacionDAO.edit(solicitudcapacitacion);
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getFlash().setKeepMessages(true);        
        context.addMessage(null, new FacesMessage("Se rechazo evaluación inicial exitosamente") );
        buscarRegistrados();
	}
	
	public void irDetalle() throws IOException{
		areaCod=null;
		cargoCod=null;
		List<PersonalCapacitacion> personalCapacitacion = selectedSolicitud.getPersonalCapacitacion();
		List<Personal> personal = new ArrayList<Personal>();
		
		for (PersonalCapacitacion personalCapacitacion2 : personalCapacitacion) {
			personal.add(personalCapacitacion2.getCodigoPersonal());
		}
		
		solicitudcapacitacion = selectedSolicitud;

		personalList = new DualListModel<Personal>(new ArrayList<Personal>(), personal);
		
		areasList = new ArrayList<Area>();
		filter.clear();
		areasList = areaDAO.findByProperty(filter);
		
		FacesContext.getCurrentInstance().getExternalContext()
		.redirect("/sistema-capacitaciones-tumi/app/solicitudCapacitacion/detalle.xhtml");
	}
	
	public void irGenerarEvaluacionInicial() throws IOException{
		
		cursosView = new ArrayList<CursoView>();
		especialidadSelected = null;
		nivelSelected = null;
		evaluacionFechaFin = null;
		
		cursoSelected = null;
		temasSelected = new ArrayList<Tema>();
		subtemasSelected = new ArrayList<Subtema>();
		temas = new ArrayList<Tema>();
		subtemas = new ArrayList<Subtema>();
		
		solicitudcapacitacion = selectedSolicitud;
		evaluacionFechaInicio = solicitudcapacitacion.getFechaInicio();
		
		filter.clear();
		especialidades = especialidadDAO.findByProperty(filter);
				
		FacesContext.getCurrentInstance().getExternalContext()
		.redirect("/sistema-capacitaciones-tumi/app/solicitudCapacitacion/generarEvaluacion.xhtml");
	}
	
	public void agregarCurso(){
		
		Nivel nivel = nivelDAO.find(nivelSelected);		
		StringBuilder mensajeDeError = new StringBuilder();
		
		if(temasSelected.size()>0 && subtemasSelected.size()==0){
			
			
			boolean found = false;
			for (Tema tema : temasSelected) {
				found = validarPersonalCapacitacioncurso(tema.getSubtemaList(), mensajeDeError);
				
				if(found){
					break;
				}
			}
			
			if (found) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.getExternalContext().getFlash().setKeepMessages(true);
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, mensajeDeError.toString(),
						null));
				return;
			}	
			
			Curso curso= temasSelected.get(0).getCodigoCurso();	
			boolean foundCursoView = false;
			for (CursoView cursoView : cursosView) {
				
				if(cursoView.getNombre().equalsIgnoreCase(curso.getNombre())){
					foundCursoView = true;
					
					for (Tema tema : temasSelected) {
						
						boolean temafound=false;
						
						for (TemaView temaView : cursoView.getTema()) {
							
							if(tema.getNombre().equalsIgnoreCase(temaView.getNombre())){
								temafound=true;
							}
							
						}
						
						if(!temafound){
							TemaView newTemaView = createTemaViewFromTema(tema, tema.getSubtemaList(), nivel);
							cursoView.getTema().add(newTemaView);
							Collections.sort(cursoView.getTema());
						}
						
					}
					
					
				}
				
			}
			
			if(!foundCursoView){
				
				CursoView newCursoView = new CursoView();
				newCursoView.setNombre(curso.getNombre());
				newCursoView.setAlcance(curso.getAlcance());
				List<TemaView> temasView = new ArrayList<TemaView>(); 
				
				for (Tema tema : temasSelected) {
					TemaView newTemaView = createTemaViewFromTema(tema, tema.getSubtemaList(), nivel);
					temasView.add(newTemaView);
				}
				Collections.sort(temasView);
				newCursoView.setTema(temasView);
				cursosView.add(newCursoView);
			}
			
		}
		
		if(temasSelected.size()==1 && subtemasSelected.size()>0){
			
			boolean found = validarPersonalCapacitacioncurso(subtemasSelected, mensajeDeError);
			
			if (found) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.getExternalContext().getFlash().setKeepMessages(true);
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, mensajeDeError.toString(),
						null));
				return;
			}	
			
			Curso curso = subtemasSelected.get(0).getCodigoTema().getCodigoCurso();
			Tema tema = temasSelected.get(0);
			boolean foundCursoView = false;
			
			//compare curso
			for (CursoView cursoView : cursosView) {
				
				if(cursoView.getNombre().equalsIgnoreCase(curso.getNombre())){
					foundCursoView=true;
					boolean foundTema=false;
					
					//compare tema
					for (TemaView temaview : cursoView.getTema()) {
						
						if(temaview.getNombre().equalsIgnoreCase(tema.getNombre())){
							foundTema=true;
							
							//compare subtema
							for (Subtema subtemaSelected : subtemasSelected) {
								
								boolean subtemaSelectedFound=false;
								
								for (SubtemaView subtema : temaview.getSubtemas()) {
									
									if(subtema.getNombre().equalsIgnoreCase(subtemaSelected.getNombre())){
										subtemaSelectedFound=true;
									}
									
								}
								
								if(!subtemaSelectedFound){
									
									SubtemaView newsubtemaView = new SubtemaView();
									newsubtemaView.setNombre(subtemaSelected.getNombre());
									newsubtemaView.setNivel(nivel.getNombre());
									newsubtemaView.setCodigoNivel(nivel.getCodigo());
									newsubtemaView.setCodigoSubtema(subtemaSelected.getCodigo());
									newsubtemaView.setOrden(subtemaSelected.getOrden());
									temaview.getSubtemas().add(newsubtemaView);
									Collections.sort(temaview.getSubtemas());
									
								}
								
							}
							
						}
						
					}
					
					if(!foundTema){
						TemaView newTemaView = new TemaView();
						newTemaView.setNombre(tema.getNombre());
						newTemaView.setAlcance(tema.getAlcance());
						newTemaView.setOrden(tema.getOrden());
						List<SubtemaView> subtemasView = new ArrayList<SubtemaView>();
						
						for (Subtema subtema : subtemasSelected) {
							SubtemaView newsubtemaView = new SubtemaView();
							newsubtemaView.setNombre(subtema.getNombre());
							newsubtemaView.setNivel(nivel.getNombre());
							newsubtemaView.setCodigoNivel(nivel.getCodigo());
							newsubtemaView.setCodigoSubtema(subtema.getCodigo());
							newsubtemaView.setOrden(subtema.getOrden());
							subtemasView.add(newsubtemaView);
							
						}

						Collections.sort(subtemasView);
						newTemaView.setSubtemas(subtemasView);
						cursoView.getTema().add(newTemaView);
						Collections.sort(cursoView.getTema());
					}
						
					
									
				}
				
			}
			
			if(!foundCursoView){
				
				CursoView newCursoView = new CursoView();
				newCursoView.setNombre(curso.getNombre());
				newCursoView.setAlcance(curso.getAlcance());
				List<TemaView> temasView = new ArrayList<TemaView>(); 
				
				TemaView newTemaView = createTemaViewFromTema(temasSelected.get(0), subtemasSelected, nivel);
				temasView.add(newTemaView);
				
				Collections.sort(temasView);
				newCursoView.setTema(temasView);
				cursosView.add(newCursoView);
				
			}
			
		}
		
		
	}

	private TemaView createTemaViewFromTema(Tema tema, List<Subtema> subtemas, Nivel nivel) {
		TemaView newTemaView = new TemaView();
		newTemaView.setNombre(tema.getNombre());
		newTemaView.setAlcance(tema.getAlcance());
		newTemaView.setOrden(tema.getOrden());
		List<SubtemaView> subtemaView = new ArrayList<SubtemaView>();
		
		for (Subtema subtema : subtemas) {
						
			SubtemaView newSubtema = new SubtemaView();
			newSubtema.setNombre(subtema.getNombre());
			newSubtema.setNivel(nivel.getNombre());
			newSubtema.setCodigoNivel(nivel.getCodigo());
			newSubtema.setCodigoSubtema(subtema.getCodigo());
			newSubtema.setOrder(subtema.getOrden());
			subtemaView.add(newSubtema);
		}
		Collections.sort(subtemaView);
		
		newTemaView.setSubtemas(subtemaView);
		return newTemaView;
	}
	
	private boolean validarPersonalCapacitacioncurso(List<Subtema> subtemas, StringBuilder mensajeDeError) {
		
		boolean found = false;//solicitudcapacitacion
		
		filter.clear();
		filter.put("codigoPersonal.codigo", PersonalLogeado.getCodigoPersonal());
		List<PersonalCapacitacion> personalCapacitaciones = personalCapacitacionDAO.findByProperty(filter);			
				
		for (PersonalCapacitacion personalCapacitacion : personalCapacitaciones) {
			
			if (personalCapacitacion.getCodigoSolicitudCapacitacion()
					.getCodigoEstado().getNombre()
					.equalsIgnoreCase("registrado")
					|| personalCapacitacion.getCodigoSolicitudCapacitacion()
							.getCodigoEstado().getNombre()
							.equalsIgnoreCase("terminado")) {
				continue;
			}
			
			boolean dateOverlap = false;
			
			if ((solicitudcapacitacion.getFechaInicio().before(
					personalCapacitacion.getCodigoSolicitudCapacitacion()
							.getFechaInicio()) && solicitudcapacitacion
					.getFechaFin().after(
							personalCapacitacion
									.getCodigoSolicitudCapacitacion()
									.getFechaInicio()))
					|| (solicitudcapacitacion.getFechaInicio().before(
							personalCapacitacion
									.getCodigoSolicitudCapacitacion()
									.getFechaFin()) && solicitudcapacitacion
							.getFechaFin().after(
									personalCapacitacion
											.getCodigoSolicitudCapacitacion()
											.getFechaFin()))
					|| (solicitudcapacitacion.getFechaInicio().before(
							personalCapacitacion
									.getCodigoSolicitudCapacitacion()
									.getFechaInicio()) && solicitudcapacitacion
							.getFechaFin().after(
									personalCapacitacion
											.getCodigoSolicitudCapacitacion()
											.getFechaFin()))) {
				dateOverlap= true;
			}
			System.out.println("=========================================================================");
			System.out.println(solicitudcapacitacion.getFechaInicio());
			System.out.println(solicitudcapacitacion.getFechaFin());
			System.out.println(personalCapacitacion.getCodigoSolicitudCapacitacion().getFechaInicio());
			System.out.println(personalCapacitacion.getCodigoSolicitudCapacitacion().getFechaFin());
			
			//fechas no se sobreponen
			if(!dateOverlap){
				continue;
			}
			
			filter.clear();
			filter.put("codigoPersonalCapacitacion.codigo", personalCapacitacion.getCodigo());
			filter.put("codigoTipo.codigo", 1);
			List<Evaluacion> evaluaciones = evaluacionDAO.findByProperty(filter);
				
			for (Evaluacion evaluacion : evaluaciones) {
					
				filter.clear();
				filter.put("codigoEvaluacion.codigo", evaluacion.getCodigo());
				List<Cursoevaluacion> cursosEvaluacion = cursoEvaluacionDAO.findByProperty(filter);
				
				for (Cursoevaluacion cursoCapacitacion : cursosEvaluacion) {
					
					for (Subtema subtema : subtemas) {
						
						if(cursoCapacitacion.getCodigoCursoNivel().getCodigoSubtema().getCodigo().compareTo(subtema.getCodigo())==0){
							found = true;
							mensajeDeError.append(personalCapacitacion
									.getCodigoPersonal().getNombre()
									+ " "
									+ personalCapacitacion.getCodigoPersonal()
											.getApellidoPaterno()
									+ " "
									+ personalCapacitacion.getCodigoPersonal()
											.getApellidoMaterno()
									+ " ya tiene asignado una capacitación de "
									+ subtema.getNombre()+ "\n");
							break;
						}
						
					}
					
					if(found){
						break;
					}
						
				}
				
				if(found){
					break;
				}
					
					
			}
				
			if(found){
				break;
			}
				
		}
		
		return found;
	}

	public void irActualizar() throws IOException{
		areaCod=null;
		cargoCod=null;
		List<PersonalCapacitacion> personalCapacitacion = selectedSolicitud.getPersonalCapacitacion();
		List<Personal> personal = new ArrayList<Personal>();
		
		for (PersonalCapacitacion personalCapacitacion2 : personalCapacitacion) {
			personal.add(personalCapacitacion2.getCodigoPersonal());
		}
		
		solicitudcapacitacion = selectedSolicitud;

		personalList = new DualListModel<Personal>(new ArrayList<Personal>(), personal);
		
		areasList = new ArrayList<Area>();
		filter.clear();
		areasList = areaDAO.findByProperty(filter);
		
		FacesContext.getCurrentInstance().getExternalContext()
		.redirect("/sistema-capacitaciones-tumi/app/solicitudCapacitacion/editar.xhtml");
	}
	
	public void onAreaChange() throws IOException {
        if(areaCod !=null){

        	cargos = new HashMap<String, String>();
        	
        	filter.clear();
        	filter.put("codigoArea.codigo", areaCod);        	
        	cargosList = cargoDAO.findByProperty(filter);
        	
        }
            
    }

	public void onTemaChange() throws IOException {
		
		if(temasSelected.size()==1){
			filter.clear();
			filter.put("codigoTema.codigo", temasSelected.get(0).getCodigo());
			subtemas = subtemaDAO.findByProperty(filter);
		} else {
			subtemas = new ArrayList<Subtema>();
		}
            
		subtemasSelected=new ArrayList<Subtema>();
    }
	
	public void onEspecialidadChange() throws IOException {
		
		if(especialidadSelected!=null){
			filter.clear();
			filter.put("codigoEspecialidad.codigo", especialidadSelected.getCodigo());
			cursos = cursoDAO.findByProperty(filter);
			
			cursoSelected = null;
			temasSelected = new ArrayList<Tema>();
			subtemasSelected = new ArrayList<Subtema>();
			temas = new ArrayList<Tema>();
			subtemas = new ArrayList<Subtema>();
		}
		
	}
	
	public void onCursoChange() throws IOException {
		
		if(cursoSelected!=null){
			filter.clear();
			filter.put("codigoCurso.codigo", cursoSelected.getCodigo());
			temas = temaDAO.findByProperty(filter);
			
			temasSelected = new ArrayList<Tema>();
			subtemasSelected = new ArrayList<Subtema>();			
			subtemas = new ArrayList<Subtema>();
		}
		
	}
	
	
	public void onCargoChange() throws IOException {
        if(areaCod !=null && cargoCod!=null){

        	cargos = new HashMap<String, String>();
        	
        	filter.clear();
        	filter.put("codigoCargo.codigo", cargoCod);        	
        	personal = personalDAO.findByProperty(filter);
        	
        	List<Personal> personalSourceList = new ArrayList<Personal>();
        	
        	for (Personal personalSource : personal) {
        		
        		boolean found= false;
        		
        		for (Personal personalTarget : personalList.getTarget()) {
					
        			if(personalTarget.getCodigo().compareTo(personalSource.getCodigo())==0){
        				found=true;
        				break;
        			}
        			
				}
        		
        		if(!found){
        			personalSourceList.add(personalSource);
        		}
				
			}
        	
        	personalList = new DualListModel<Personal>(personalSourceList, personalList.getTarget());
        	
        }
            
    }
	
	public void handleTransfer(TransferEvent event) {
		System.out.println(event);
        //event.getItems() : List of items transferred
        //event.isAdd() : Is transfer from source to target
        //event.isRemove() : Is transfer from target to source
    }
	
	public void guardar() throws IOException, ParseException {
		
		Estado estadoRegistrado = estadoDAO.find(3);
		
		solicitudcapacitacion.setUsuarioRegistro("admmin");
		solicitudcapacitacion.setCodigoEstado(estadoRegistrado);
		solicitudCapacitacionDAO.create(solicitudcapacitacion);
		
		List<PersonalCapacitacion> personalCapacitacionList = new ArrayList<PersonalCapacitacion>();
		
		for (Personal personal : personalList.getTarget()) {
			
			PersonalCapacitacion personalCapacitacion = new PersonalCapacitacion();
			personalCapacitacion.setCodigoPersonal(personal);
			personalCapacitacion.setCodigoSolicitudCapacitacion(solicitudcapacitacion);
			personalCapacitacionList.add(personalCapacitacion);
			personalCapacitacion.setCodigoEstado(estadoRegistrado);
			
			Date todayWithZeroTime = formatter.parse(formatter.format(today));
			
			personalCapacitacion.setFechaRegistro(todayWithZeroTime);
			personalCapacitacion.setUsuarioRegistro("admin");
			personalCapacitacionDAO.create(personalCapacitacion);
		}
		
		
		solicitudcapacitacion.setPersonalCapacitacion(personalCapacitacionList);

		filter.clear();
		solicitudes = solicitudCapacitacionDAO.findByProperty(filter);
	
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getFlash().setKeepMessages(true);        
        context.addMessage(null, new FacesMessage("Se registro Solicitud de Capacitacion exitosamente con el codigo: "+ solicitudcapacitacion.getCodigo()) );
        context.getExternalContext().redirect("/sistema-capacitaciones-tumi/app/solicitudCapacitacion/consultar.xhtml");
    }
	
	public void guardarCursos() throws IOException, ParseException {
		
		if(cursosView.isEmpty()){
			FacesContext context = FacesContext.getCurrentInstance();	        
	        context.addMessage(null, new FacesMessage("Error: No hay Cursos seleccionado") );
	        return;
		}
		
		LocalDate solicitudInicio = solicitudcapacitacion.getFechaInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate solicitudFin = solicitudcapacitacion.getFechaFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate evaluacionFin = evaluacionFechaFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		long noOfDaysBetween = ChronoUnit.DAYS.between(solicitudInicio, solicitudFin);
		noOfDaysBetween = noOfDaysBetween/2;
		LocalDate dateWith50Percent = solicitudInicio.plusDays(noOfDaysBetween);
		
		if(evaluacionFin.isAfter(dateWith50Percent)){
			FacesContext context = FacesContext.getCurrentInstance();	        
	        context.addMessage(null, new FacesMessage("Error: Fecha de Fin de Evaluación no puede ser"
	        		+ "mayor al rango de 50% de dias entre fecha inicio y fin de Solicitud de Capacitación") );
	        return;
		}
		
		Estado capacitacionRegistrado = estadoDAO.find(3);
		Estado registrado = estadoDAO.find(3);
		Tipoevaluacion evaluacionInicial = tipoevaluacionDAO.find(1);
		
		Integer numeroPreguntas = Integer.valueOf(TumiPropertyValues.getInstance().getProperty("numeroPreguntas"));
		Integer minutosDuracion = Integer.valueOf(TumiPropertyValues.getInstance().getProperty("minutosDuracion"));
		
		for (PersonalCapacitacion personalCapacitacion : solicitudcapacitacion.getPersonalCapacitacion()) {
			
			personalCapacitacion.setCodigoEstado(capacitacionRegistrado);
			personalCapacitacion.setUsuarioModificacion("admin");
			personalCapacitacion.setFechaModificacion(new Date());
			personalCapacitacionDAO.edit(personalCapacitacion);
			
			Date todayWithZeroTime = formatter.parse(formatter.format(today));
		
			Evaluacion newEvaluacion = new Evaluacion();
			newEvaluacion.setCodigoPersonalCapacitacion(personalCapacitacion);
			newEvaluacion.setCodigoEstado(registrado);
			newEvaluacion.setCodigoTipo(evaluacionInicial);
			newEvaluacion.setFechaInicio(Date.from(solicitudInicio.atStartOfDay(ZoneId.systemDefault()).toInstant()));
			newEvaluacion.setFechaFin(Date.from(evaluacionFin.atStartOfDay(ZoneId.systemDefault()).toInstant()));
			newEvaluacion.setMinutosDuracion(minutosDuracion);
			newEvaluacion.setNumeroPregunta(numeroPreguntas);
			
			newEvaluacion.setFechaRegistro(todayWithZeroTime);
			newEvaluacion.setUsuarioRegistro("admin");
			
			List<Cursoevaluacion> cursosEvaluacion = new ArrayList<Cursoevaluacion>();
			
			for (CursoView cursoView : cursosView) {
				
				for (TemaView temaView : cursoView.getTema()) {
					
					for (SubtemaView subtemaview : temaView.getSubtemas()) {
						
						filter.clear();
						filter.put("codigoNivel.codigo", subtemaview.getCodigoNivel());
						filter.put("codigoSubtema.codigo", subtemaview.getCodigoSubtema());
						List<Cursonivel> cursoNivel = cursoNivelDAO.findByProperty(filter);
						
						Cursoevaluacion cursoEvaluacion = new Cursoevaluacion();
						cursoEvaluacion.setCodigoCursoNivel(cursoNivel.get(0));
						cursoEvaluacion.setCodigoEvaluacion(newEvaluacion);

						cursoEvaluacion.setFechaRegistro(todayWithZeroTime);
						cursoEvaluacion.setUsuarioRegistro("admin");
						cursosEvaluacion.add(cursoEvaluacion);
						//cursoEvaluacionDAO.create(cursoEvaluacion);
						filter.clear();
						filter.put("codigoCursoNivel.codigo", cursoNivel.get(0).getCodigo());
						List<Pregunta> preguntas = preguntasDAO.findByProperty(filter);
						Collections.shuffle(preguntas);
						List<Cursoevaluacionpregunta> evaluacionPregunta = new ArrayList<Cursoevaluacionpregunta>();
						List<Pregunta> preguntasSeleccionadas = new ArrayList<Pregunta>();
						
						for (int i = 0; i < numeroPreguntas; i++) {
							
							Pregunta preguntaSelected = getRandomItem(preguntas);
							
							while (preguntasSeleccionadas.contains(preguntaSelected)) {
								preguntaSelected = getRandomItem(preguntas);
							}
							
							//Pregunta preguntaSelected = getRandomItem(preguntas);	//se esta agregagando doble pregunta						
							Cursoevaluacionpregunta pregunta = new Cursoevaluacionpregunta();
							pregunta.setCodigoCursoEvaluacion(cursoEvaluacion);
							pregunta.setCodigoPregunta(preguntaSelected);
							pregunta.setFechaRegistro(todayWithZeroTime);
							pregunta.setUsuarioRegistro("admin");
							evaluacionPregunta.add(pregunta);
							preguntasSeleccionadas.add(preguntaSelected);
						}
						cursoEvaluacion.setCursoevaluacionpreguntaList(evaluacionPregunta);
					}
					
				}
				
			}
			
			newEvaluacion.setCursoevaluacionList(cursosEvaluacion);
			evaluacionDAO.create(newEvaluacion);
			
		}
						
		Estado codigoEstado = estadoDAO.find(1);//aprobado
		solicitudcapacitacion.setCodigoEstado(codigoEstado);
		solicitudCapacitacionDAO.edit(solicitudcapacitacion);
		
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getFlash().setKeepMessages(true);        
        context.addMessage(null, new FacesMessage("Se genero evaluación inicial exitosamente") );
        buscarRegistrados();
	}
	
	private Pregunta getRandomItem(List<Pregunta> list)
	{
	    Random random = new Random();
	    int listSize = list.size();
	    int randomIndex = random.nextInt(listSize);
	    return list.get(randomIndex);
	}
	
	public Area getArea() throws IOException{
		return area;
	}
	
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	
	public String getCargo() {
		return cargo;
	}
	
	public Map<String, String> getAreas() {
		return areas;
	}
	
	public Map<String, String> getCargos() {
		return cargos;
	}
	
	public AreaDAO getAreaDAO() {
		return areaDAO;
	}
	
	public List<Area> getAreasList() {
		return areasList;
	}
	
	public void setAreasList(List<Area> areasList) {
		this.areasList = areasList;
	}
	
	public Solicitudcapacitacion getSolicitudcapacitacion() {
		return solicitudcapacitacion;
	}
	
	public void setSolicitudcapacitacion(
			Solicitudcapacitacion solicitudcapacitacion) {
		this.solicitudcapacitacion = solicitudcapacitacion;
	}
	
	public void setArea(Area area) {
		this.area = area;
	}
	public List<Cargo> getCargosList() {
		return cargosList;
	}
	
	public void setCargosList(List<Cargo> cargosList) {
		this.cargosList = cargosList;
	}
	
	public Integer getAreaCod() {
		return areaCod;
	}
    
    public void setAreaCod(Integer areaCod) {
		this.areaCod = areaCod;
	}
    
    public Integer getCargoCod() {
		return cargoCod;
	}
    
    public void setCargoCod(Integer cargoCod) {
		this.cargoCod = cargoCod;
	}
    
    public DualListModel<Personal> getPersonalList() {
		return personalList;
	}
    
    public void setPersonalList(DualListModel<Personal> personalList) {
		this.personalList = personalList;
	}
	public List<Solicitudcapacitacion> getSolicitudes() {
		return solicitudes;
	}
	public void setSolicitudes(List<Solicitudcapacitacion> solicitudes) {
		this.solicitudes = solicitudes;
	}
	
	public Solicitudcapacitacion getSelectedSolicitud() {
		return selectedSolicitud;
	}
	
	public void setSelectedSolicitud(Solicitudcapacitacion selectedSolicitud) {
		this.selectedSolicitud = selectedSolicitud;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
	public Integer getEstado() {
		return estado;
	}
	
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	public void setSelectedTest(List<String> selectedTest) {
		this.selectedTest = selectedTest;
	}
	
	public List<String> getSelectedTest() {
		return selectedTest;
	}
	
	public List<String> getSelectedTest2() {
		return selectedTest2;
	}
	
	public void setSelectedTest2(List<String> selectedTest2) {
		this.selectedTest2 = selectedTest2;
	}

	public List<Especialidad> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(List<Especialidad> especialidades) {
		this.especialidades = especialidades;
	}

	public Especialidad getEspecialidadSelected() {
		return especialidadSelected;
	}

	public void setEspecialidadSelected(Especialidad especialidadSelected) {
		this.especialidadSelected = especialidadSelected;
	}

	public List<Curso> getCursos() {
		return cursos;
	}

	public void setCursos(List<Curso> cursos) {
		this.cursos = cursos;
	}

	public Curso getCursoSelected() {
		return cursoSelected;
	}

	public void setCursoSelected(Curso cursoSelected) {
		this.cursoSelected = cursoSelected;
	}

	public List<Tema> getTemas() {
		return temas;
	}

	public void setTemas(List<Tema> temas) {
		this.temas = temas;
	}


	public List<Subtema> getSubtemas() {
		return subtemas;
	}

	public void setSubtemas(List<Subtema> subtemas) {
		this.subtemas = subtemas;
	}
	
	public List<Tema> getTemasSelected() {
		return temasSelected;
	}

	public void setTemasSelected(List<Tema> temasSelected) {
		this.temasSelected = temasSelected;
	}

	public List<Subtema> getSubtemasSelected() {
		return subtemasSelected;
	}

	public void setSubtemasSelected(List<Subtema> subtemasSelected) {
		this.subtemasSelected = subtemasSelected;
	}

	public Especialidad getEspecialidadForConverter(Integer valueOf) {
		for (Especialidad especialidad : especialidades) {
				
			if(especialidad.getCodigo().compareTo(valueOf)==0){
				return especialidad;
			}
				
		}
		return null;
	}

	public Curso getCursoForConverter(Integer valueOf) {
		for (Curso curso : cursos) {
			
			if(curso.getCodigo().compareTo(valueOf)==0){
				return curso;
			}
			
		}
		return null;
	}

	public Tema getTemaForConverter(Integer valueOf) {
		for (Tema tema : temas) {
			
			if(tema.getCodigo().compareTo(valueOf)==0){
				return tema;
			}
			
		}
		return null;
	}

	public Subtema getSubTemaForConverter(Integer valueOf) {
		for (Subtema subtema : subtemas) {
			
			if(subtema.getCodigo().compareTo(valueOf)==0){
				return subtema;
			}
			
		}
		return null;
	}
	
	public List<CursoView> getCursosView() {
		return cursosView;
	}
	
	public void setCursosView(List<CursoView> cursosView) {
		this.cursosView = cursosView;
	}
	
	public Integer getNivelSelected() {
		return nivelSelected;
	}
	
	public void setNivelSelected(Integer nivelSelected) {
		this.nivelSelected = nivelSelected;
	}
	
	public void setCursoEliminar(CursoView cursoEliminar) {
		this.cursoEliminar = cursoEliminar;
	}
	
	public CursoView getCursoEliminar() {
		return cursoEliminar;
	}

	public Date getEvaluacionFechaInicio() {
		return evaluacionFechaInicio;
	}

	public void setEvaluacionFechaInicio(Date evaluacionFechaInicio) {
		this.evaluacionFechaInicio = evaluacionFechaInicio;
	}

	public Date getEvaluacionFechaFin() {
		return evaluacionFechaFin;
	}

	public void setEvaluacionFechaFin(Date evaluacionFechaFin) {
		this.evaluacionFechaFin = evaluacionFechaFin;
	}
	
	public String getComentarioRechazo() {
		return comentarioRechazo;
	}
	
	public void setComentarioRechazo(String comentarioRechazo) {
		this.comentarioRechazo = comentarioRechazo;
	}
	
	public Date getMaxFechaInicio() {
		return maxFechaInicio;
	}
	
	public void setMaxFechaInicio(Date maxFechaInicio) {
		this.maxFechaInicio = maxFechaInicio;
	}
}
