package com.sv.tumi.view.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sv.tumi.db.dao.CursoCapacitacionDAO;
import com.sv.tumi.db.dao.CursoEvaluacionDAO;
import com.sv.tumi.db.dao.CursoNivelDAO;
import com.sv.tumi.db.dao.CursoevaluacionPreguntaRespuestaDAO;
import com.sv.tumi.db.dao.CursoevaluacionpreguntaDAO;
import com.sv.tumi.db.dao.EstadoDAO;
import com.sv.tumi.db.dao.EvaluacionDAO;
import com.sv.tumi.db.dao.NivelDAO;
import com.sv.tumi.db.dao.ParametroGeneracionCapacitacionDAO;
import com.sv.tumi.db.dao.PatronDesarrolloDAO;
import com.sv.tumi.db.dao.PersonalCapacitacionDAO;
import com.sv.tumi.db.dao.PreguntasDAO;
import com.sv.tumi.db.dao.RespuestaDAO;
import com.sv.tumi.db.dao.ResultadoEvaluacionDAO;
import com.sv.tumi.db.dao.SolicitudCapacitacionDAO;
import com.sv.tumi.db.dao.SubtemaDAO;
import com.sv.tumi.db.dao.TemaDAO;
import com.sv.tumi.db.dao.TiempoDesarrolloDAO;
import com.sv.tumi.db.dao.TipoevaluacionDAO;
import com.sv.tumi.db.entity.Curso;
import com.sv.tumi.db.entity.Cursocapacitacion;
import com.sv.tumi.db.entity.Cursoevaluacion;
import com.sv.tumi.db.entity.Cursoevaluacionpregunta;
import com.sv.tumi.db.entity.Cursoevaluacionpreguntarespuesta;
import com.sv.tumi.db.entity.Cursonivel;
import com.sv.tumi.db.entity.Estado;
import com.sv.tumi.db.entity.Evaluacion;
import com.sv.tumi.db.entity.Nivel;
import com.sv.tumi.db.entity.Patrondesarrollo;
import com.sv.tumi.db.entity.Personal;
import com.sv.tumi.db.entity.PersonalCapacitacion;
import com.sv.tumi.db.entity.Pregunta;
import com.sv.tumi.db.entity.Respuesta;
import com.sv.tumi.db.entity.Resultadoevaluacion;
import com.sv.tumi.db.entity.Subtema;
import com.sv.tumi.db.entity.Tema;
import com.sv.tumi.db.entity.Tiempodesarrollo;
import com.sv.tumi.db.entity.Tipoevaluacion;
import com.sv.tumi.neuroph.TumiMultiLayerPerceptron;
import com.sv.tumi.neuroph.util.NormalizacionUtil;
import com.sv.tumi.util.TumiPropertyValues;
import com.sv.tumi.view.CursoView;
import com.sv.tumi.view.SubtemaView;
import com.sv.tumi.view.TemaView;

@ManagedBean(name = "evaluacionBean", eager = true)
@SessionScoped
public class EvaluacionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory
			.getLogger(EvaluacionBean.class);
	private Pregunta pregunta;
	private Evaluacion selectedEvaluacion;
	private Tiempodesarrollo selectedTiempo;

	@ManagedProperty(name = "preguntaIndex", value = "0")
	private Integer preguntaIndex = 0;
	private Boolean showfinalizar = false;
	private int number = 10000;
	private int resueltoIndice;

	List<Cursoevaluacion> cursoEvaluacionList = new ArrayList<Cursoevaluacion>();
	List<Cursocapacitacion> cursoCapacitacion = new ArrayList<Cursocapacitacion>();
	List<Cursoevaluacionpregunta> preguntasEvaluacion = new ArrayList<Cursoevaluacionpregunta>();
	List<Cursoevaluacionpreguntarespuesta> cursoevaluacionpreguntarespuestas = new ArrayList<Cursoevaluacionpreguntarespuesta>();
	List<Cursonivel> cursosGenerados = new ArrayList<Cursonivel>();
	Map<String, Object> filter = new HashMap<String, Object>();
	List<Evaluacion> evaluacionesRegistradas = new ArrayList<Evaluacion>();
	List<Evaluacion> evaluacionesResueltas = new ArrayList<Evaluacion>();
	Evaluacion selectedEvaluacionRegistrada = new Evaluacion();
	Evaluacion selectedEvaluacionResuelta = new Evaluacion();
	private Date fechaResueltoDesde;
	private Date fechaResueltoHasta;
	List<PersonalCapacitacion> personalCapacitaciones;
	PersonalCapacitacion personalCapacitacionSelected;
	private Integer inicioPregunta;

	private String selectedItem;
	private List<String> selectedItems = new ArrayList();

	PatronDesarrolloDAO patronDesarrolloDAO = new PatronDesarrolloDAO();
	TiempoDesarrolloDAO tiempoDesarrolloDAO = new TiempoDesarrolloDAO();
	CursoevaluacionpreguntaDAO cursoevaluacionpreguntaDAO = new CursoevaluacionpreguntaDAO();
	CursoEvaluacionDAO cursoEvaluacionDAO = new CursoEvaluacionDAO();
	TipoevaluacionDAO tipoevaluacionDAO = new TipoevaluacionDAO();
	RespuestaDAO respuestaDAO = new RespuestaDAO();
	ResultadoEvaluacionDAO resultadoEvaluacionDAO = new ResultadoEvaluacionDAO();
	CursoevaluacionPreguntaRespuestaDAO cursoevaluacionPreguntaRespuestaDAO = new CursoevaluacionPreguntaRespuestaDAO();
	EstadoDAO estadoDAO = new EstadoDAO();
	EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
	PersonalCapacitacionDAO personalCapacitacionDAO = new PersonalCapacitacionDAO();
	ParametroGeneracionCapacitacionDAO parametroGeneracionCapacitacionDAO = new ParametroGeneracionCapacitacionDAO();
	NivelDAO nivelDAO = new NivelDAO();
	CursoNivelDAO cursoNivelDAO = new CursoNivelDAO();
	CursoCapacitacionDAO cursoCapacitacionDAO = new CursoCapacitacionDAO();
	PreguntasDAO preguntasDAO = new PreguntasDAO();

	TemaDAO temaDAO = new TemaDAO();
	SubtemaDAO subtemaDAO = new SubtemaDAO();
	SolicitudCapacitacionDAO solicitudCapacitacionDAO = new SolicitudCapacitacionDAO();
	List<CursoView> cursosTemario = new ArrayList<CursoView>();
	List<CursoView> cursosGeneradosCapacitacion = new ArrayList<CursoView>();
	BarChartModel barModel;

	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	@PostConstruct
	public void inicializar() {

		barModel = initBarModel();

		barModel.setTitle("Cuadro de Aprobación de Cursos");
		barModel.setLegendPosition("ne");

		Axis xAxis = barModel.getAxis(AxisType.X);
		xAxis.setLabel("Curso");

		Axis yAxis = barModel.getAxis(AxisType.Y);
		yAxis.setLabel("Cantidad Personal");
		yAxis.setMin(0);
		yAxis.setMax(200);
	}

	public BarChartModel getBarModel() {
		return barModel;
	}

	public void setBarModel(BarChartModel barModel) {
		this.barModel = barModel;
	}

	private BarChartModel initBarModel() {
		BarChartModel model = new BarChartModel();

		ChartSeries boys = new ChartSeries();
		boys.setLabel("Cantidad de Aprobados");
		boys.set("Tesoreria", 120);
		boys.set("Apoyo al socio", 100);
		boys.set("Cobranzas", 44);
		boys.set("Creditos", 150);
		boys.set("Gestión de socios", 25);

		ChartSeries girls = new ChartSeries();
		girls.setLabel("Cantidad de Desaprobados");
		girls.set("Tesoreria", 52);
		girls.set("Apoyo al socio", 60);
		girls.set("Cobranzas", 110);
		girls.set("Creditos", 135);
		girls.set("Gestión de socios", 120);

		model.addSeries(boys);
		model.addSeries(girls);

		return model;
	}

	public void cargaEvaluacion() {
		// selectedEvaluacion.setFechaInicio(new Date());

		showfinalizar = false;// reset value
		pregunta = null;// reset value
		preguntasEvaluacion = new ArrayList<Cursoevaluacionpregunta>();// reset
																		// value
		preguntaIndex = 0;// reset value
		cursoevaluacionpreguntarespuestas = new ArrayList<Cursoevaluacionpreguntarespuesta>();// reset
																								// value
		resueltoIndice = 1;
		number = selectedEvaluacion.getMinutosDuracion();

		filter.clear();
		filter.put("codigoEvaluacion.codigo", selectedEvaluacion.getCodigo());
		// cursoEvaluacionList = cursoEvaluacionDAO.findByProperty(filter);
		cursoEvaluacionList = selectedEvaluacion.getCursoevaluacionList();

		filter.clear();
		for (Cursoevaluacion cursoevaluacion : cursoEvaluacionList) {

			filter.put("codigoCursoEvaluacion.codigo",
					cursoevaluacion.getCodigo());
			List<Cursoevaluacionpregunta> preguntasCursos = cursoevaluacionpreguntaDAO
					.findByProperty(filter);

			for (Cursoevaluacionpregunta preguntasCurso : preguntasCursos) {
				preguntasEvaluacion.add(preguntasCurso);
			}

			cursoevaluacion.setCursoevaluacionpreguntaList(preguntasCursos);
		}

		cursosTemario = new ArrayList<CursoView>();
		getCursosEvaluacion(cursosTemario, true);

	}

	public void goPreguntas() throws IOException {

		if (showfinalizar) {
			finalizarEvaluacion();
		} else if (preguntasEvaluacion.size() - 1 == getPreguntaIndex()) {
			showfinalizar = true;
		}

		agregarPregunta();

		pregunta = preguntasEvaluacion.get(getPreguntaIndex())
				.getCodigoPregunta();
		
		if (selectedEvaluacion.getCodigoTipo().getCodigo().compareTo(2) == 0) {
			inicioPregunta = number;
			preguntasEvaluacion.get(getPreguntaIndex())
					.setPatrondesarrolloList(new ArrayList<Patrondesarrollo>());

		}
		
		setPreguntaIndex(getPreguntaIndex() + 1);

		filter.clear();
		filter.put("codigoPregunta.codigo", pregunta.getCodigo());
		List<Respuesta> respuestas = respuestaDAO.findByProperty(filter);
		pregunta.setRespuestaList(respuestas);

		limpiarRespuesta();
		FacesContext.getCurrentInstance().getExternalContext()
				.redirect("pregunta.xhtml");
	}

	private void limpiarRespuesta() {
		selectedItem = null;
		selectedItems = new ArrayList();
	}

	private void agregarPregunta() {
		if (pregunta != null) {
			if (pregunta.getCodigoTipoPregunta().getCodigo() == 1) {
				valorarPregunta(selectedItem);
				crearRespuesta(selectedItem);
			}

			if (pregunta.getCodigoTipoPregunta().getCodigo() == 2) {
				valorarPregunta(selectedItems);
				for (String selectedItem : selectedItems) {
					crearRespuesta(selectedItem);
				}

			}
			if (pregunta.getCodigoTipoPregunta().getCodigo() == 3) {
				// crearRespuesta(null);
				preguntasEvaluacion.get(getPreguntaIndex() - 1)
						.setPuntajeObtenido(0.0);
			}

			if (selectedEvaluacion.getCodigoTipo().getCodigo().compareTo(2) == 0) {

				List<Tiempodesarrollo> tiempos = new ArrayList<Tiempodesarrollo>();
				Tiempodesarrollo tiempoDesarrollo = new Tiempodesarrollo();
				tiempoDesarrollo
						.setCodigoCursoEvaluacionPregunta(preguntasEvaluacion
								.get(getPreguntaIndex() - 1));
				tiempoDesarrollo.setUsuarioRegistro("admin");

				try {
					Date todayWithZeroTime = formatter.parse(formatter
							.format(new Date()));

					tiempoDesarrollo.setFechaRegistro(todayWithZeroTime);
				} catch (Exception e) {
					e.printStackTrace();
				}

				tiempoDesarrollo.setSegundosTiempoPregunta(inicioPregunta
						- number);
				System.out.println("segundos pasado : =======> "
						+ (inicioPregunta - number));
				tiempos.add(tiempoDesarrollo);

				preguntasEvaluacion.get(getPreguntaIndex() - 1)
						.setTiempodesarrolloList(tiempos);

			}

		}

	}

	public void finalizarEvaluacion() throws IOException {

		agregarPregunta();

		for (Cursoevaluacion cursoevaluacion : cursoEvaluacionList) {

			Double puntajeObtenido = 0.0;
			Double maximoPuntaje = 0.0;
			Double porcentajeAprobacion = 0.0;
			for (Cursoevaluacionpregunta cursoevaluacionpregunta : preguntasEvaluacion) {

				if (cursoevaluacionpregunta.getCodigoCursoEvaluacion()
						.getCodigo().compareTo(cursoevaluacion.getCodigo()) != 0) {
					continue;
				}

				maximoPuntaje = maximoPuntaje
						+ cursoevaluacionpregunta.getCodigoPregunta()
								.getValor();

				if (cursoevaluacionpregunta.getPuntajeObtenido() != null
						&& cursoevaluacionpregunta.getPuntajeObtenido()
								.compareTo(new Double(0.0)) != 0) {
					puntajeObtenido = puntajeObtenido
							+ cursoevaluacionpregunta.getPuntajeObtenido();
				}
				cursoevaluacionpreguntaDAO.edit(cursoevaluacionpregunta);

				if (selectedEvaluacion.getCodigoTipo().getCodigo().compareTo(2) == 0) {
					for (Tiempodesarrollo tiempodesarrollo : cursoevaluacionpregunta
							.getTiempodesarrolloList()) {
						tiempoDesarrolloDAO.create(tiempodesarrollo);
					}

					for (Patrondesarrollo patrondesarrollo : cursoevaluacionpregunta
							.getPatrondesarrolloList()) {
						patronDesarrolloDAO.create(patrondesarrollo);
					}

				}

			}

			System.out.println(puntajeObtenido);
			System.out.println(maximoPuntaje);
			porcentajeAprobacion = (puntajeObtenido * 100) / maximoPuntaje;

			Nivel bajo = nivelDAO.find(1);
			Nivel intermedio = nivelDAO.find(2);
			Nivel alto = nivelDAO.find(3);

			for (Cursoevaluacionpreguntarespuesta cursoevaluacionpreguntarespuesta : cursoevaluacionpreguntarespuestas) {

				if (cursoevaluacionpreguntarespuesta
						.getCodigoCursoEvaluacionPregunta()
						.getCodigoCursoEvaluacion().getCodigo()
						.compareTo(cursoevaluacion.getCodigo()) != 0)
					continue;

				cursoevaluacionPreguntaRespuestaDAO
						.create(cursoevaluacionpreguntarespuesta);
			}

			Resultadoevaluacion resultadoevaluacion = new Resultadoevaluacion();
			resultadoevaluacion.setCodigoCursoEvaluacion(cursoevaluacion);
			resultadoevaluacion.setFechaRegistro(new Date());
			resultadoevaluacion.setUsuarioRegistro("1");
			resultadoevaluacion.setPuntaje(porcentajeAprobacion);

			if (porcentajeAprobacion < 70) {
				resultadoevaluacion.setCodigoNivel(bajo);
			}
			if (porcentajeAprobacion > 70 && porcentajeAprobacion < 90) {
				resultadoevaluacion.setCodigoNivel(intermedio);
			}
			if (porcentajeAprobacion > 90) {
				resultadoevaluacion.setCodigoNivel(alto);
			}
			resultadoEvaluacionDAO.create(resultadoevaluacion);

		}

		Estado estadoEvaluacion = estadoDAO.find(4);// evaluacion resuelto
		selectedEvaluacion.setCodigoEstado(estadoEvaluacion);
		selectedEvaluacion.setFechaModificacion(new Date());
		selectedEvaluacion.setUsuarioModificacion("1");
		evaluacionDAO.edit(selectedEvaluacion);

		if (selectedEvaluacion.getCodigoTipo().getCodigo().compareTo(1) == 0) {// evaluacion
																				// inicial
			Estado estadoPersonalCapacitacion = estadoDAO.find(6);// personal
			// 'evaluacion
			// inicial
			// resuelto'
			actualizarEstadodePersonalCapacitacion(estadoPersonalCapacitacion);
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("evaluacionFinal.xhtml");
		} else if (selectedEvaluacion.getCodigoTipo().getCodigo().compareTo(2) == 0) {// evaluacion
																						// capacitacion
			Estado terminado = estadoDAO.find(7);// terminado
			actualizarEstadodePersonalCapacitacion(terminado);

			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.redirect(
							"/sistema-capacitaciones-tumi/app/capacitacion/final.xhtml");
		}

	}

	private void actualizarEstadodePersonalCapacitacion(
			Estado estadoPersonalCapacitacion) {
		PersonalCapacitacion personalCapacitacion = selectedEvaluacion
				.getCodigoPersonalCapacitacion();
		personalCapacitacion.setFechaModificacion(new Date());
		personalCapacitacion.setUsuarioModificacion("1");
		personalCapacitacion.setCodigoEstado(estadoPersonalCapacitacion);
		personalCapacitacionDAO.edit(personalCapacitacion);
	}

	private PersonalCapacitacion personalCapacitacion;

	public PersonalCapacitacion getPersonalCapacitacion() {
		return personalCapacitacion;
	}

	public void setPersonalCapacitacion(
			PersonalCapacitacion personalCapacitacion) {
		this.personalCapacitacion = personalCapacitacion;
	}

	public void goExamenesRegistrados() throws IOException {
		evaluacionesRegistradas.clear();
		filter.clear();
		filter.put("codigoEstado.codigo", 3);// evaluacion registrada
		filter.put("codigoTipo.codigo", 1);// evaluacion inicial
		// filter.put("codigoPersonalCapacitacion.codigoEstado.codigo",
		// 5);//personal capacitacion 'capacitacion generado'
		// filter.put("codigoPersonalCapacitacion.codigoSolicitudCapacitacion.codigoEstado.codigo",
		// 1);//solicitud capacitacion aprobado
		List<Evaluacion> evaluaciones = evaluacionDAO.findByProperty(filter);

		for (Evaluacion evaluacion : evaluaciones) {

			if (evaluacion.getCodigoPersonalCapacitacion().getCodigoEstado()
					.getCodigo().compareTo(3) == 0
					&& // registrado
					evaluacion.getCodigoPersonalCapacitacion()
							.getCodigoSolicitudCapacitacion().getCodigoEstado()
							.getCodigo().compareTo(1) == 0) {// aprobado
				evaluacionesRegistradas.add(evaluacion);
			}

		}

		FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.redirect(
						"/sistema-capacitaciones-tumi/app/evaluacionInicial/consultarRegistrados.xhtml");
	}

	public void goEvaluacionesRegistrados() throws IOException {
		personalCapacitaciones = new ArrayList<PersonalCapacitacion>();
		filter.clear();

		List<PersonalCapacitacion> personalCapacitaciones = personalCapacitacionDAO
				.findByProperty(filter);

		for (PersonalCapacitacion personalCapacitacion : personalCapacitaciones) {

			if (personalCapacitacion.getCodigoEstado().getCodigo().compareTo(8) == 0 // capacitacion
																						// visto
					|| personalCapacitacion.getCodigoEstado().getCodigo()
							.compareTo(9) == 0) {// evaluacion capacitacion
													// generado
				this.personalCapacitaciones.add(personalCapacitacion);
			}

		}

		FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.redirect(
						"/sistema-capacitaciones-tumi/app/evaluacionCapacitacion/consultar.xhtml");
	}

	public void generarEvaluacionCapacitacion() throws IOException,
			ParseException {

		filter.clear();
		filter.put("codigoPersonalCapacitacion.codigo",
				personalCapacitacionSelected.getCodigo());
		filter.put("codigoEstado.codigo", 3);// registrado
		filter.put("codigoTipo.codigo", 2);// evaluacion capacitacion
		List<Evaluacion> evaluaciones = evaluacionDAO.findByProperty(filter);

		if (evaluaciones.isEmpty()) {
			Integer numeroPreguntas = Integer.valueOf(TumiPropertyValues
					.getInstance().getProperty("numeroPreguntas"));
			Integer minutosDuracion = Integer.valueOf(TumiPropertyValues
					.getInstance().getProperty("minutosDuracion"));

			Estado registrado = estadoDAO.find(3);// registrado
			Estado evaluacionCapacitacionGenerado = estadoDAO.find(9);// evaluacion
																		// capacitacion
																		// generado
			Tipoevaluacion evaluacionCapacitacion = tipoevaluacionDAO.find(2);// evaluacion
																				// capacitacion

			personalCapacitacionSelected
					.setCodigoEstado(evaluacionCapacitacionGenerado);
			personalCapacitacionSelected.setUsuarioModificacion("admin");
			personalCapacitacionSelected.setFechaModificacion(new Date());
			personalCapacitacionDAO.edit(personalCapacitacionSelected);

			Evaluacion newEvaluacion = new Evaluacion();
			newEvaluacion
					.setCodigoPersonalCapacitacion(personalCapacitacionSelected);
			newEvaluacion.setCodigoEstado(registrado);
			newEvaluacion.setCodigoTipo(evaluacionCapacitacion);
			newEvaluacion.setFechaInicio(new Date());
			newEvaluacion.setFechaFin(personalCapacitacionSelected
					.getCodigoSolicitudCapacitacion().getFechaFin());
			newEvaluacion.setMinutosDuracion(minutosDuracion);
			newEvaluacion.setNumeroPregunta(numeroPreguntas);

			Date todayWithZeroTime = formatter.parse(formatter
					.format(new Date()));
			newEvaluacion.setFechaRegistro(todayWithZeroTime);
			newEvaluacion.setUsuarioRegistro("admin");

			filter.clear();
			filter.put("codigoPersonalCapacitacion.codigo",
					personalCapacitacionSelected.getCodigo());
			List<Cursocapacitacion> cursosCapacitacion = cursoCapacitacionDAO
					.findByProperty(filter);

			List<Cursoevaluacion> cursosEvaluacion = new ArrayList<Cursoevaluacion>();

			for (Cursocapacitacion cursocapacitacion : cursosCapacitacion) {

				Cursoevaluacion cursoEvaluacion = new Cursoevaluacion();
				cursoEvaluacion.setCodigoCursoNivel(cursocapacitacion
						.getCodigoCursoNivel());
				cursoEvaluacion.setCodigoEvaluacion(newEvaluacion);

				cursoEvaluacion.setFechaRegistro(todayWithZeroTime);
				cursoEvaluacion.setUsuarioRegistro("admin");
				cursosEvaluacion.add(cursoEvaluacion);

				filter.clear();
				filter.put("codigoCursoNivel.codigo", 2);// TODO:repplace with
															// cursocapacitacion.getCodigoCursoNivel().getCodigo()
															// , need add more
															// questions
				List<Pregunta> preguntas = preguntasDAO.findByProperty(filter);
				Collections.shuffle(preguntas);
				List<Cursoevaluacionpregunta> evaluacionPregunta = new ArrayList<Cursoevaluacionpregunta>();
				List<Pregunta> preguntasSeleccionadas = new ArrayList<Pregunta>();

				for (int i = 0; i < numeroPreguntas; i++) {

					Pregunta preguntaSelected = getRandomItem(preguntas);

					while (preguntasSeleccionadas.contains(preguntaSelected)) {
						preguntaSelected = getRandomItem(preguntas);
					}

					Cursoevaluacionpregunta pregunta = new Cursoevaluacionpregunta();
					pregunta.setCodigoCursoEvaluacion(cursoEvaluacion);
					pregunta.setCodigoPregunta(preguntaSelected);
					pregunta.setFechaRegistro(todayWithZeroTime);
					pregunta.setUsuarioRegistro("admin");
					evaluacionPregunta.add(pregunta);
					preguntasSeleccionadas.add(preguntaSelected);
				}
				cursoEvaluacion
						.setCursoevaluacionpreguntaList(evaluacionPregunta);
			}

			newEvaluacion.setCursoevaluacionList(cursosEvaluacion);
			evaluacionDAO.create(newEvaluacion);

			selectedEvaluacionRegistrada = newEvaluacion;
		} else {
			selectedEvaluacionRegistrada = evaluaciones.get(0);
		}

		goIniciarExamen();
	}

	private Pregunta getRandomItem(List<Pregunta> list) {
		Random random = new Random();
		int listSize = list.size();
		int randomIndex = random.nextInt(listSize);
		return list.get(randomIndex);
	}

	public void goExamenesResueltos() throws IOException {
		evaluacionesResueltas.clear();
		filter.clear();
		filter.put("codigoEstado.codigo", 4);// evaluacion resuelto
		// filter.put("codigoPersonalCapacitacion.codigoEstado.codigo", 6);//
		// personal de capacitacion en evaluacion inicial resuelto
		List<Evaluacion> evaluaciones = evaluacionDAO.findByProperty(filter);

		for (Evaluacion evaluacion : evaluaciones) {

			if (evaluacion.getCodigoPersonalCapacitacion().getCodigoEstado()
					.getCodigo().compareTo(6) == 0) {
				evaluacionesResueltas.add(evaluacion);
			}

		}

		FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.redirect(
						"/sistema-capacitaciones-tumi/app/evaluacionInicial/consultarResueltos.xhtml");
	}

	public void goIniciarExamen() throws IOException {
		selectedEvaluacion = selectedEvaluacionRegistrada;
		cargaEvaluacion();
		FacesContext.getCurrentInstance().getExternalContext()
				.redirect("/sistema-capacitaciones-tumi/app/evaluacion.xhtml");
	}

	public void goGenerarCapacitacion() throws IOException {
		selectedEvaluacion = selectedEvaluacionResuelta;
		gogenerarcapacitacion();
	}

	public void goConsultarResueltos() throws IOException {
		filter.clear();
		filter.put("codigoEstado.codigo", 4);// evaluacion resuelto
		evaluacionesRegistradas = evaluacionDAO.findByProperty(filter);
		FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.redirect(
						"/sistema-capacitaciones-tumi/app/evaluacion/consultar.xhtml");
	}

	public void goEvaluacionDetalle() throws IOException {
		selectedEvaluacion = selectedEvaluacionRegistrada;

		filter.clear();
		filter.put("codigoEvaluacion.codigo", selectedEvaluacion.getCodigo());
		cursoEvaluacionList = cursoEvaluacionDAO.findByProperty(filter);
		personalCapacitacion = selectedEvaluacion
				.getCodigoPersonalCapacitacion();

		if (personalCapacitacion.getCodigoEstado().getCodigo() == 6
				|| personalCapacitacion.getCodigoEstado().getCodigo() == 5) {
			cursosTemario = new ArrayList<CursoView>();
			getCursosEvaluacion(cursosTemario, false);
		}

		cursosGeneradosCapacitacion.clear();// necesario por ahora

		FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.redirect(
						"/sistema-capacitaciones-tumi/app/evaluacion/detalle.xhtml");
	}

	public void filtrarResueltoPorFechas() throws IOException {
		evaluacionesRegistradas = evaluacionDAO.obtenerPorFechas(
				fechaResueltoDesde, fechaResueltoHasta);
		FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.redirect(
						"/sistema-capacitaciones-tumi/app/evaluacion/consultar.xhtml");
	}

	public void gogenerarcapacitacion() throws IOException {

		filter.clear();
		filter.put("codigoEvaluacion.codigo", selectedEvaluacion.getCodigo());
		cursoEvaluacionList = cursoEvaluacionDAO.findByProperty(filter);
		personalCapacitacion = selectedEvaluacion
				.getCodigoPersonalCapacitacion();

		if (personalCapacitacion.getCodigoEstado().getCodigo() == 6
				|| personalCapacitacion.getCodigoEstado().getCodigo() == 5) {
			cursosTemario = new ArrayList<CursoView>();
			getCursosEvaluacion(cursosTemario, false);
		}

		FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.redirect(
						"/sistema-capacitaciones-tumi/app/generarCapacitacion.xhtml");
	}

	private void getCursosEvaluacion(List<CursoView> cursos,
			boolean evaluacionInicial) {
		for (Cursoevaluacion cursoevaluacion : cursoEvaluacionList) {

			Curso curso = cursoevaluacion.getCodigoCursoNivel()
					.getCodigoSubtema().getCodigoTema().getCodigoCurso();
			Tema tema = cursoevaluacion.getCodigoCursoNivel()
					.getCodigoSubtema().getCodigoTema();
			Subtema subtema = cursoevaluacion.getCodigoCursoNivel()
					.getCodigoSubtema();

			String nivel = null;
			Double puntaje = null;

			if (evaluacionInicial) {
				nivel = cursoevaluacion.getCodigoCursoNivel().getCodigoNivel()
						.getNombre();
				puntaje = 0.0;
			} else {

				if (cursoevaluacion.getResultadoevaluacion() != null) {
					nivel = cursoevaluacion.getResultadoevaluacion()
							.getCodigoNivel().getNombre();
					puntaje = cursoevaluacion.getResultadoevaluacion()
							.getPuntaje();
				} else {
					nivel = "";
					puntaje = 0.0;
				}

			}

			getCurso(cursos, curso, tema, subtema, nivel, puntaje);

		}

	}

	private void getCursosCapacitacion(List<CursoView> cursos) {
		for (Cursocapacitacion cursoevaluacion : cursoCapacitacion) {

			Curso curso = cursoevaluacion.getCodigoCursoNivel()
					.getCodigoSubtema().getCodigoTema().getCodigoCurso();
			Tema tema = cursoevaluacion.getCodigoCursoNivel()
					.getCodigoSubtema().getCodigoTema();
			Subtema subtema = cursoevaluacion.getCodigoCursoNivel()
					.getCodigoSubtema();

			getCurso(cursos, curso, tema, subtema, cursoevaluacion
					.getCodigoCursoNivel().getCodigoNivel().getNombre(), 0.0);

		}

	}

	private void getCurso(List<CursoView> cursos, Curso curso, Tema tema,
			Subtema subtema, String nombreNivel, double puntaje) {
		CursoView cursoView = null;
		TemaView temaView = null;
		SubtemaView subtemaView = null;

		// CURSO

		for (CursoView cursoViewItem : cursos) {

			if (curso.getNombre().equalsIgnoreCase(cursoViewItem.getNombre())) {
				cursoView = cursoViewItem;
			}

		}

		if (cursoView == null) {
			cursoView = new CursoView();
			cursoView.setNombre(curso.getNombre());
			cursoView.setAlcance(curso.getAlcance());
			cursos.add(cursoView);
		}

		// TEMA

		for (TemaView temaViewItem : cursoView.getTema()) {

			if (tema.getNombre().equalsIgnoreCase(temaViewItem.getNombre())) {
				temaView = temaViewItem;
			}

		}

		if (temaView == null) {
			temaView = new TemaView();
			temaView.setNombre(tema.getNombre());
			temaView.setAlcance(tema.getAlcance());
			temaView.setOrden(tema.getOrden());
			cursoView.getTema().add(temaView);
		}

		// SUB TEMA

		for (SubtemaView subtemaViewItem : temaView.getSubtemas()) {

			if (subtema.getNombre().equalsIgnoreCase(
					subtemaViewItem.getNombre())
					&& nombreNivel.equalsIgnoreCase(subtemaViewItem.getNivel())) {
				subtemaView = subtemaViewItem;
			}

		}

		if (subtemaView == null) {
			subtemaView = new SubtemaView();
			subtemaView.setNombre(subtema.getNombre());
			subtemaView.setNivel(nombreNivel);
			subtemaView.setOrder(subtema.getOrden());
			subtemaView.setPorcentajeAprobacion(puntaje);
			temaView.getSubtemas().add(subtemaView);
		}
	}

	public void generarCapacitacion() throws IOException {
		cursosGenerados = new ArrayList<Cursonivel>();

		Personal personal = personalCapacitacion.getCodigoPersonal();
		int nivelDesempeñoId = personal.getCodigoNivelDesempeño().getCodigo();
		int nivelPersonalId = personal.getCodigoNivelPersonal().getCodigo();

		for (Cursoevaluacion cursoevaluacion : cursoEvaluacionList) {

			filter.clear();
			filter.put("codigoCursoEvaluacion.codigo",
					cursoevaluacion.getCodigo());
			Resultadoevaluacion resultadoEvalacion = resultadoEvaluacionDAO
					.findByProperty(filter).get(0);

			System.out.println("cargo "
					+ personalCapacitacion.getCodigoPersonal().getCodigoCargo()
							.getCodigo());

			System.out.println("sub tema "
					+ cursoevaluacion.getCodigoCursoNivel().getCodigoSubtema()
							.getCodigo());

			System.out.println("resultado nivel (nivel actual) "
					+ resultadoEvalacion.getCodigoNivel().getCodigo());

			System.out.println("nivel depemseño " + nivelDesempeñoId);

			System.out.println("nivel personal " + nivelPersonalId);

			TumiMultiLayerPerceptron rn = new TumiMultiLayerPerceptron();
			// usar red neuronal. input cargo, area, curso, nivel actual. output
			// nivel selecionado
			double[] nivelSelecionadoArray = rn.getPrediction(NormalizacionUtil
					.getCargoValue(personalCapacitacion.getCodigoPersonal()
							.getCodigoCargo().getCodigo()), NormalizacionUtil
					.getSubtemaValue(cursoevaluacion.getCodigoCursoNivel()
							.getCodigoSubtema().getCodigo()), NormalizacionUtil
					.getNivelValue(resultadoEvalacion.getCodigoNivel()
							.getCodigo()), NormalizacionUtil
					.getNivelDesempeñoValue(nivelDesempeñoId),
					NormalizacionUtil.getNivelPersonalValue(nivelPersonalId));

			int nivelSelecionadoId = getNivelId(nivelSelecionadoArray);
			System.out.println("nivel selecionado " + nivelSelecionadoId);

			// for (int i = nivelSelecionadoId; i <=
			// cursoevaluacion.getCodigoCursoNivel()
			// .getCodigoNivel().getCodigo(); i++) {

			filter.clear();
			filter.put("codigoNivel.codigo", nivelSelecionadoId);
			filter.put("codigoSubtema.codigo", cursoevaluacion
					.getCodigoCursoNivel().getCodigoSubtema().getCodigo());

			Cursonivel cursonivelSeleccionado = cursoNivelDAO.findByProperty(
					filter).get(0);

			Cursocapacitacion cursocapacitacion = new Cursocapacitacion();
			cursocapacitacion
					.setCodigoPersonalCapacitacion(personalCapacitacion);
			cursocapacitacion.setCodigoCursoNivel(cursonivelSeleccionado);
			cursocapacitacion.setFechaRegistro(new Date());
			cursocapacitacion.setUsuarioRegistro("1");
			cursoCapacitacionDAO.create(cursocapacitacion);
			cursoCapacitacion.add(cursocapacitacion);

			// }

		}

		Estado estado = estadoDAO.find(5);// generado caacitacion
		personalCapacitacion.setCodigoEstado(estado);
		personalCapacitacionDAO.edit(personalCapacitacion);

		getCursosCapacitacion(cursosGeneradosCapacitacion);
		// return "/app/generarCapacitacion?faces-redirect=true";
		FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.redirect(
						"/sistema-capacitaciones-tumi/app/generarCapacitacion.xhtml");

	}

	private int getNivelId(double[] nivelSelecionado) {
		int maxAt = 0;

		for (int i = 0; i < nivelSelecionado.length; i++) {
			maxAt = nivelSelecionado[i] > nivelSelecionado[maxAt] ? i : maxAt;
		}

		return maxAt + 1;
	}

	private void valorarPregunta(String codigoRespuesta) {

		Cursoevaluacionpregunta pregunta = preguntasEvaluacion
				.get(getPreguntaIndex() - 1);

		if (codigoRespuesta != null) {

			boolean esVerdadero = esRespuestaValida(codigoRespuesta);

			if (esVerdadero) {
				pregunta.setPuntajeObtenido(pregunta.getCodigoPregunta()
						.getValor());
			} else {
				pregunta.setPuntajeObtenido(0.0);
			}
		} else {
			pregunta.setPuntajeObtenido(0.0);
		}

	}

	private boolean esRespuestaValida(String codigoRespuesta) {
		boolean esVerdadero = false;
		Respuesta respuesta = respuestaDAO.find(Integer
				.valueOf(codigoRespuesta));

		if (respuesta.getVerdadero() == 1) {
			esVerdadero = true;
		}
		return esVerdadero;
	}

	private void valorarPregunta(List<String> codigoRespuestas) {
		Cursoevaluacionpregunta pregunta = preguntasEvaluacion
				.get(getPreguntaIndex() - 1);

		if (codigoRespuestas != null && codigoRespuestas.size() > 0) {
			boolean exito = esRespuestaValida(codigoRespuestas, pregunta);

			if (exito) {
				pregunta.setPuntajeObtenido(pregunta.getCodigoPregunta()
						.getValor());
			} else {
				pregunta.setPuntajeObtenido(0.0);
			}
		} else {
			pregunta.setPuntajeObtenido(0.0);
		}

	}

	private boolean esRespuestaValida(List<String> codigoRespuestas,
			Cursoevaluacionpregunta pregunta) {
		boolean exito = true;

		filter.clear();
		filter.put("codigoPregunta.codigo", pregunta.getCodigo());
		filter.put("verdadero", new Short("1"));
		List<Respuesta> respuestas = respuestaDAO.findByProperty(filter);

		if (Integer.valueOf(respuestas.size()).compareTo(
				Integer.valueOf(codigoRespuestas.size())) != 0) {
			exito = false;
		} else {
			for (Respuesta respuesta : respuestas) {

				boolean found = false;

				for (String codigoRespuesta : codigoRespuestas) {

					if (Integer.valueOf(codigoRespuesta).compareTo(
							respuesta.getCodigo()) == 0) {
						found = true;
						break;
					}

				}

				if (!found) {
					exito = false;
					break;
				}

			}
		}
		return exito;
	}

	private void crearRespuesta(String codigoRespuesta) {

		if (codigoRespuesta != null) {
			Cursoevaluacionpreguntarespuesta cursoevaluacionpreguntarespuesta = new Cursoevaluacionpreguntarespuesta();
			cursoevaluacionpreguntarespuesta
					.setCodigoCursoEvaluacionPregunta(preguntasEvaluacion
							.get(getPreguntaIndex() - 1));
			cursoevaluacionpreguntarespuesta.setCodigoRespuesta(respuestaDAO
					.find(Integer.valueOf(codigoRespuesta)));
			cursoevaluacionpreguntarespuesta.setFechaRegistro(new Date());
			cursoevaluacionpreguntarespuesta.setUsuarioRegistro("1");
			cursoevaluacionpreguntarespuestas
					.add(cursoevaluacionpreguntarespuesta);
		}

	}

	public void actualizarPatronDesarrollo() {
		if (selectedEvaluacion.getCodigoTipo().getCodigo().compareTo(2) == 0) {
			Patrondesarrollo patron;
			// numero de intentos
			if (preguntasEvaluacion.get(getPreguntaIndex()-1)
					.getPatrondesarrolloList().isEmpty()) {
				patron = new Patrondesarrollo();
				patron.setCodigoCursoEvaluacionPregunta(preguntasEvaluacion
						.get(getPreguntaIndex()-1));
				patron.setFechaRegistro(new Date());
				patron.setUsuarioRegistro("admin");
				patron.setNumeroIntentos(1);// primer intento
				patron.setOrdenResuelto(resueltoIndice);
				resueltoIndice++;
				preguntasEvaluacion.get(getPreguntaIndex()-1)
						.getPatrondesarrolloList().add(patron);
			} else {
				patron = preguntasEvaluacion.get(getPreguntaIndex()-1)
						.getPatrondesarrolloList().get(0);
				patron.setNumeroIntentos(patron.getNumeroIntentos()+1);//sumando intento
			}

			// intento valido
			if (pregunta.getCodigoTipoPregunta().getCodigo() == 1) {
				boolean exito = esRespuestaValida(selectedItem);

				if (exito) {
					patron.setResultadoIntento(patron.getNumeroIntentos());
				} else {
					patron.setResultadoIntento(null);
				}

			} else if (pregunta.getCodigoTipoPregunta().getCodigo() == 2) {
				valorarPregunta(selectedItems);
				for (String selectedItem : selectedItems) {
					crearRespuesta(selectedItem);
				}

				boolean exito = esRespuestaValida(selectedItems,
						preguntasEvaluacion.get(getPreguntaIndex()-1));

				if (exito) {
					patron.setResultadoIntento(patron.getNumeroIntentos());
				} else {
					patron.setResultadoIntento(null);
				}

			}

		}

	}

	public String goEmpleadoMovin() throws IOException {
		Flash flash = FacesContext.getCurrentInstance().getExternalContext()
				.getFlash();
		// flash.put("selectedEmpl", selectedEmpleado);
		return "empleado_movimiento.xhtml?faces-redirect=true";
	}

	public Evaluacion getSelectedEvaluacion() {
		return selectedEvaluacion;
	}

	public void setSelectedEvaluacion(Evaluacion selectedEvaluacion) {
		this.selectedEvaluacion = selectedEvaluacion;
	}

	public Tiempodesarrollo getSelectedTiempo() {
		return selectedTiempo;
	}

	public void setSelectedTiempo(Tiempodesarrollo selectedTiempo) {
		this.selectedTiempo = selectedTiempo;
	}

	public Integer getPreguntaIndex() {
		return preguntaIndex;
	}

	public void setPreguntaIndex(Integer preguntaIndex) {
		this.preguntaIndex = preguntaIndex;
	}

	public List<Cursoevaluacionpregunta> getPreguntasEvaluacion() {
		return preguntasEvaluacion;
	}

	public void setPreguntasEvaluacion(
			List<Cursoevaluacionpregunta> preguntasEvaluacion) {
		this.preguntasEvaluacion = preguntasEvaluacion;
	}

	public Pregunta getPregunta() {
		// this.pregunta=getPreguntasEvaluacion().get(preguntaIndex);
		// this.pregunta.setRespuestaList(getPreguntasEvaluacion().get(preguntaIndex).getRespuestaList());
		// preguntaIndex++;
		return pregunta;
	}

	public void setPregunta(Pregunta pregunta) {
		this.pregunta = pregunta;
	}

	public String getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
	}

	public List<String> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(List<String> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public Boolean getShowfinalizar() {
		return showfinalizar;
	}

	public List<Cursonivel> getCursosGenerados() {
		return cursosGenerados;
	}

	public void setCursosGenerados(List<Cursonivel> cursosGenerados) {
		this.cursosGenerados = cursosGenerados;
	}

	public List<Cursoevaluacion> getCursoEvaluacionList() {
		return cursoEvaluacionList;
	}

	public int getNumber() throws IOException {
		return number;
	}

	public String getNumberFormat() {
		return getDateFromMillis(number);
	}

	public void increment() throws IOException {
		if (number == 0) {
			finalizarEvaluacion();
		}
		number = number - 1000;
	}

	public List<CursoView> getCursosTemario() {
		return cursosTemario;
	}

	public void setCursosTemario(List<CursoView> cursosTemario) {
		this.cursosTemario = cursosTemario;
	}

	public List<CursoView> getCursosGeneradosCapacitacion() {
		return cursosGeneradosCapacitacion;
	}

	public void setCursosGeneradosCapacitacion(
			List<CursoView> cursosGeneradosCapacitacion) {
		this.cursosGeneradosCapacitacion = cursosGeneradosCapacitacion;
	}

	public static String getDateFromMillis(long millis) {
		return new SimpleDateFormat("mm:ss").format(new Date(millis));
	}

	public List<Evaluacion> getEvaluacionesRegistradas() {
		return evaluacionesRegistradas;
	}

	public void setEvaluacionesRegistradas(
			List<Evaluacion> evaluacionesRegistradas) {
		this.evaluacionesRegistradas = evaluacionesRegistradas;
	}

	public List<Evaluacion> getEvaluacionesResueltas() {
		return evaluacionesResueltas;
	}

	public void setEvaluacionesResueltas(List<Evaluacion> evaluacionesResueltas) {
		this.evaluacionesResueltas = evaluacionesResueltas;
	}

	public Evaluacion getSelectedEvaluacionRegistrada() {
		return selectedEvaluacionRegistrada;
	}

	public void setSelectedEvaluacionRegistrada(
			Evaluacion selectedEvaluacionRegistrada) {
		this.selectedEvaluacionRegistrada = selectedEvaluacionRegistrada;
	}

	public Evaluacion getSelectedEvaluacionResuelta() {
		return selectedEvaluacionResuelta;
	}

	public void setSelectedEvaluacionResuelta(
			Evaluacion selectedEvaluacionResuelta) {
		this.selectedEvaluacionResuelta = selectedEvaluacionResuelta;
	}

	public Date getFechaResueltoDesde() {
		return fechaResueltoDesde;
	}

	public void setFechaResueltoDesde(Date fechaResueltoDesde) {
		this.fechaResueltoDesde = fechaResueltoDesde;
	}

	public Date getFechaResueltoHasta() {
		return fechaResueltoHasta;
	}

	public void setFechaResueltoHasta(Date fechaResueltoHasta) {
		this.fechaResueltoHasta = fechaResueltoHasta;
	}

	public List<PersonalCapacitacion> getPersonalCapacitaciones() {
		return personalCapacitaciones;
	}

	public void setPersonalCapacitaciones(
			List<PersonalCapacitacion> personalCapacitaciones) {
		this.personalCapacitaciones = personalCapacitaciones;
	}

	public PersonalCapacitacion getPersonalCapacitacionSelected() {
		return personalCapacitacionSelected;
	}

	public void setPersonalCapacitacionSelected(
			PersonalCapacitacion personalCapacitacionSelected) {
		this.personalCapacitacionSelected = personalCapacitacionSelected;
	}

	public Integer getInicioPregunta() {
		return inicioPregunta;
	}

	public void setInicioPregunta(Integer inicioPregunta) {
		this.inicioPregunta = inicioPregunta;
	}

	public int getResueltoIndice() {
		return resueltoIndice;
	}

	public void setResueltoIndice(int resueltoIndice) {
		this.resueltoIndice = resueltoIndice;
	}

}
