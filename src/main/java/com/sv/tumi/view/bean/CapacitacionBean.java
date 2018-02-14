package com.sv.tumi.view.bean;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.hibernate.Hibernate;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.MeterGaugeChartModel;
import org.primefaces.model.chart.PieChartModel;

import com.sv.tumi.db.dao.CursoCapacitacionDAO;
import com.sv.tumi.db.dao.CursoNivelDAO;
import com.sv.tumi.db.dao.CursoevaluacionpreguntaDAO;
import com.sv.tumi.db.dao.EstadoDAO;
import com.sv.tumi.db.dao.EvaluacionDAO;
import com.sv.tumi.db.dao.PersonalCapacitacionDAO;
import com.sv.tumi.db.dao.RespuestaDAO;
import com.sv.tumi.db.entity.Curso;
import com.sv.tumi.db.entity.Cursocapacitacion;
import com.sv.tumi.db.entity.Cursoevaluacion;
import com.sv.tumi.db.entity.Cursoevaluacionpregunta;
import com.sv.tumi.db.entity.Cursonivel;
import com.sv.tumi.db.entity.Estado;
import com.sv.tumi.db.entity.Evaluacion;
import com.sv.tumi.db.entity.Patrondesarrollo;
import com.sv.tumi.db.entity.PersonalCapacitacion;
import com.sv.tumi.db.entity.Pregunta;
import com.sv.tumi.db.entity.Respuesta;
import com.sv.tumi.db.entity.Subtema;
import com.sv.tumi.db.entity.Tema;
import com.sv.tumi.db.entity.Tiempodesarrollo;
import com.sv.tumi.view.CursoTreeView;
import com.sv.tumi.view.CursoView;
import com.sv.tumi.view.DistribucionTiempoView;
import com.sv.tumi.view.IntentoPreguntaView;
import com.sv.tumi.view.OrdenPreguntaView;
import com.sv.tumi.view.PreguntaView;
import com.sv.tumi.view.RangosTiempoPreguntasView;
import com.sv.tumi.view.SubtemaView;
import com.sv.tumi.view.TemaView;

@ManagedBean(name = "capacitacionBean", eager = true)
@SessionScoped
public class CapacitacionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CursoevaluacionpreguntaDAO cursoevaluacionpreguntaDAO = new CursoevaluacionpreguntaDAO();
	private CursoCapacitacionDAO cursoCapacitacionDAO = new CursoCapacitacionDAO();
	private CursoNivelDAO cursoNivelDAO = new CursoNivelDAO();
	private EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
	RespuestaDAO respuestaDAO = new RespuestaDAO();
	private EstadoDAO estadoDAO = new EstadoDAO();
	private PersonalCapacitacionDAO personalCapacitacionDAO = new PersonalCapacitacionDAO();
	List<PersonalCapacitacion> personalCapacitaciones;
	PersonalCapacitacion personalCapacitacionSelected;
	private TreeNode capacitacionTree;
	private TreeNode cursoSeleccionado;
	private Map<String, Object> filter = new HashMap<String, Object>();
	private Cursonivel cursonivel;
	private StreamedContent streamedContent;
	private List<Cursocapacitacion> cursosCapacitacion;
	private List<Evaluacion> evaluacionesResueltas = new ArrayList<Evaluacion>();
	private Evaluacion selectedEvaluacionResuelta;
	private List<PreguntaView> preguntas = new ArrayList<PreguntaView>();
	private List<DistribucionTiempoView> distribucionSubtemas = new ArrayList<DistribucionTiempoView>();
	private List<RangosTiempoPreguntasView> rangosTiempos = new ArrayList<RangosTiempoPreguntasView>();
	private PieChartModel distribucionDeCursos = new PieChartModel();
	private List<OrdenPreguntaView> ordenPreguntas = new ArrayList<OrdenPreguntaView>();
	private List<IntentoPreguntaView> intentoPreguntas = new ArrayList<IntentoPreguntaView>();
	private List<DistribucionTiempoView> distribucionSubtemasAprobados = new ArrayList<DistribucionTiempoView>();
	private List<DistribucionTiempoView> distribucionSubtemasDesaprobados = new ArrayList<DistribucionTiempoView>();

	// //////// FOR UPLOAD TEST*//////////////////

	private UploadedFile file;
	private Integer codigoCurso;

	public void upload() {
		if (file != null) {
			Cursonivel curso = cursoNivelDAO.find(codigoCurso);
			curso.setContenido(file.getContents());
			curso.setFormatoContenido("pdf");
			cursoNivelDAO.edit(curso);

			FacesMessage message = new FacesMessage("Succesful"
					+ file.getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public Integer getCodigoCurso() {
		return codigoCurso;
	}

	public void setCodigoCurso(Integer codigoCurso) {
		this.codigoCurso = codigoCurso;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	// ///////////////////////////////////////////

	@PostConstruct
	public void init() {
		capacitacionTree = new DefaultTreeNode();
	}

	public void goVerCapacitaciones() throws IOException {

		filter.clear();
		filter.put("codigoEstado.codigo", 5);// capacitacion generado
		personalCapacitaciones = personalCapacitacionDAO.findByProperty(filter);

		FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.redirect(
						"/sistema-capacitaciones-tumi/app/capacitacion/consultar.xhtml");
	}

	public void goVerCursos() throws IOException {

		filter.clear();
		filter.put("codigoPersonalCapacitacion.codigo",
				personalCapacitacionSelected.getCodigo());
		cursosCapacitacion = cursoCapacitacionDAO.findByProperty(filter);

		List<CursoView> cursosView = new ArrayList<CursoView>();
		getCursosCapacitacion(cursosView, cursosCapacitacion);

		capacitacionTree = new DefaultTreeNode();

		for (CursoView cursoView : cursosView) {

			TreeNode cursoNode = new DefaultTreeNode(new CursoTreeView(
					cursoView.getNombre(), cursoView.getAlcance(), false),
					capacitacionTree);

			for (TemaView temaView : cursoView.getTema()) {

				TreeNode temaNode = new DefaultTreeNode(new CursoTreeView(
						temaView.getNombre(), temaView.getAlcance(), false),
						cursoNode);

				for (SubtemaView subtemaView : temaView.getSubtemas()) {
					TreeNode subtemaNode = new DefaultTreeNode(
							new CursoTreeView(subtemaView.getNombre(),
									subtemaView.getAlcance(), true,
									subtemaView.getCodigoNivel(),
									subtemaView.getCodigoSubtema()), temaNode);
				}
			}
		}

		FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.redirect(
						"/sistema-capacitaciones-tumi/app/capacitacion/cursos.xhtml");
	}

	public void goVerContenido() throws IOException {

		if (cursoSeleccionado == null) {
			return;
		}

		CursoTreeView curso = (CursoTreeView) cursoSeleccionado.getData();

		if (curso.isEnable()) {

			filter.clear();
			filter.put("codigoNivel.codigo", curso.getCodigoNivel());
			filter.put("codigoSubtema.codigo", curso.getCodigoSubtema());
			List<Cursonivel> cursosNivel = cursoNivelDAO.findByProperty(filter);
			cursonivel = cursosNivel.get(0);

			for (Cursocapacitacion cursoCapacitacion : cursosCapacitacion) {
				if (cursoCapacitacion.getCodigoCursoNivel().getCodigo()
						.compareTo(cursonivel.getCodigo()) == 0) {
					cursoCapacitacion.setVisto(true);
					cursoCapacitacionDAO.edit(cursoCapacitacion);
				}
			}

			boolean vistoTodos = true;

			for (Cursocapacitacion cursoCapacitacion : cursosCapacitacion) {
				if (!cursoCapacitacion.isVisto()) {
					vistoTodos = false;
					break;
				}

			}

			if (vistoTodos) {
				Estado visto = estadoDAO.find(8);
				personalCapacitacionSelected.setCodigoEstado(visto);
				personalCapacitacionDAO.edit(personalCapacitacionSelected);
			}

			streamedContent = new DefaultStreamedContent(
					getData(cursonivel.getContenido()), "application/pdf");

			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.redirect(
							"/sistema-capacitaciones-tumi/app/capacitacion/contenido.xhtml");
		}

	}

	public StreamedContent getPdfDocument() {
		return getStreamedContent();
	}

	public String generateRandomIdForNotCaching() {
		return java.util.UUID.randomUUID().toString();
	}

	private InputStream getData(byte[] content) {

		InputStream is = null;
		is = new ByteArrayInputStream(content);
		is.mark(0);
		return is;

	}

	private void getCursosCapacitacion(List<CursoView> cursos,
			List<Cursocapacitacion> cursoCapacitacionList) {
		for (Cursocapacitacion cursoevaluacion : cursoCapacitacionList) {

			Curso curso = cursoevaluacion.getCodigoCursoNivel()
					.getCodigoSubtema().getCodigoTema().getCodigoCurso();
			Tema tema = cursoevaluacion.getCodigoCursoNivel()
					.getCodigoSubtema().getCodigoTema();
			Cursonivel subtema = cursoevaluacion.getCodigoCursoNivel();

			getCurso(cursos, curso, tema, subtema);

		}

	}

	private void getCurso(List<CursoView> cursos, Curso curso, Tema tema,
			Cursonivel cursoNivel) {
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

			if (cursoNivel.getCodigoSubtema().getNombre()
					.equalsIgnoreCase(subtemaViewItem.getNombre())) {
				subtemaView = subtemaViewItem;
			}

		}

		if (subtemaView == null) {
			subtemaView = new SubtemaView();
			subtemaView.setNombre(cursoNivel.getCodigoSubtema().getNombre());
			subtemaView.setOrder(cursoNivel.getCodigoSubtema().getOrden());
			subtemaView.setAlcance(cursoNivel.getAlcance());
			subtemaView.setCodigoNivel(cursoNivel.getCodigoNivel().getCodigo());
			subtemaView.setCodigoSubtema(cursoNivel.getCodigoSubtema()
					.getCodigo());
			temaView.getSubtemas().add(subtemaView);
		}
	}

	public void goAnalizarTiempo() throws IOException {
		filter.clear();
		filter.put("codigoTipo.codigo", 2);
		filter.put("codigoEstado.codigo", 4);
		evaluacionesResueltas = evaluacionDAO.findByProperty(filter);
		FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.redirect(
						"/sistema-capacitaciones-tumi/app/capacitacion/analisisTiempoResueltos.xhtml");
	}

	public void goAnalizarPatron() throws IOException {
		filter.clear();
		filter.put("codigoTipo.codigo", 2);
		filter.put("codigoEstado.codigo", 4);
		evaluacionesResueltas = evaluacionDAO.findByProperty(filter);
		FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.redirect(
						"/sistema-capacitaciones-tumi/app/capacitacion/analisisPatronResueltos.xhtml");
	}

	public void goPatronDetalle() throws IOException {

		ordenPreguntas = new ArrayList<OrdenPreguntaView>();
		intentoPreguntas = new ArrayList<IntentoPreguntaView>();
		distribucionSubtemasAprobados = new ArrayList<DistribucionTiempoView>();
		distribucionSubtemasDesaprobados = new ArrayList<DistribucionTiempoView>();

		for (Cursoevaluacion cursoevaluacion : selectedEvaluacionResuelta
				.getCursoevaluacionList()) {

			if (cursoevaluacion.getResultadoevaluacion().getPuntaje() > 80) {
				distribucionSubtemasAprobados.add(new DistribucionTiempoView(
						cursoevaluacion.getCodigoCursoNivel()
								.getCodigoSubtema().getCodigoTema()
								.getCodigoCurso().getNombre(), cursoevaluacion
								.getCodigoCursoNivel().getCodigoSubtema()
								.getCodigoTema().getNombre(), cursoevaluacion
								.getCodigoCursoNivel().getCodigoSubtema()
								.getNombre(), cursoevaluacion
								.getResultadoevaluacion().getPuntaje()));
			} else {
				distribucionSubtemasDesaprobados
						.add(new DistribucionTiempoView(cursoevaluacion
								.getCodigoCursoNivel().getCodigoSubtema()
								.getCodigoTema().getCodigoCurso().getNombre(),
								cursoevaluacion.getCodigoCursoNivel()
										.getCodigoSubtema().getCodigoTema()
										.getNombre(), cursoevaluacion
										.getCodigoCursoNivel()
										.getCodigoSubtema().getNombre(),
								cursoevaluacion.getResultadoevaluacion()
										.getPuntaje()));
			}

			for (Cursoevaluacionpregunta evaluacionPregunta : cursoevaluacion
					.getCursoevaluacionpreguntaList()) {

				Patrondesarrollo patronDesarrollo = (evaluacionPregunta
						.getPatrondesarrolloList() != null && evaluacionPregunta
						.getPatrondesarrolloList().size() >= 1) ? evaluacionPregunta
						.getPatrondesarrolloList().get(0) : null;

				ordenPreguntas
						.add(new OrdenPreguntaView(cursoevaluacion
								.getCodigoCursoNivel().getCodigoSubtema()
								.getCodigoTema().getCodigoCurso().getNombre(),
								cursoevaluacion.getCodigoCursoNivel()
										.getCodigoSubtema().getCodigoTema()
										.getNombre(), cursoevaluacion
										.getCodigoCursoNivel()
										.getCodigoSubtema().getNombre(),
								evaluacionPregunta.getCodigoPregunta()
										.getTexto(),
								(patronDesarrollo != null) ? patronDesarrollo
										.getNumeroIntentos() : null));

				String esCorrecto = (evaluacionPregunta.getPuntajeObtenido() != null && evaluacionPregunta
						.getPuntajeObtenido() >= 1) ? "si" : "no";

				filter.clear();
				filter.put("codigoPregunta.codigo", evaluacionPregunta
						.getCodigoPregunta().getCodigo());
				filter.put("verdadero", new Short("1"));
				List<Respuesta> respuestas = respuestaDAO
						.findByProperty(filter);

				intentoPreguntas
						.add(new IntentoPreguntaView(cursoevaluacion
								.getCodigoCursoNivel().getCodigoSubtema()
								.getCodigoTema().getCodigoCurso().getNombre(),
								cursoevaluacion.getCodigoCursoNivel()
										.getCodigoSubtema().getCodigoTema()
										.getNombre(), cursoevaluacion
										.getCodigoCursoNivel()
										.getCodigoSubtema().getNombre(),
								evaluacionPregunta.getCodigoPregunta()
										.getTexto(),
								(patronDesarrollo != null) ? patronDesarrollo
										.getNumeroIntentos() : null,
								(patronDesarrollo != null) ? patronDesarrollo
										.getOrdenResuelto() : null, esCorrecto,
								evaluacionPregunta.getCodigoPregunta()
										.getCodigoTipoPregunta().getNombre(),
								respuestas.size()));
			}
		}

		FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.redirect(
						"/sistema-capacitaciones-tumi/app/capacitacion/analisisPatron.xhtml");
	}

	public void goTiempoDetalle() throws IOException {

		preguntas = new ArrayList<PreguntaView>();
		distribucionSubtemas = new ArrayList<DistribucionTiempoView>();
		rangosTiempos = new ArrayList<RangosTiempoPreguntasView>();
		double totalDuracion = 0;

		for (Cursoevaluacion cursoevaluacion : selectedEvaluacionResuelta
				.getCursoevaluacionList()) {

			filter.clear();
			filter.put("codigoCursoEvaluacion.codigo",
					cursoevaluacion.getCodigo());
			List<Cursoevaluacionpregunta> evaluacionPregunta = cursoevaluacionpreguntaDAO
					.findByProperty(filter);
			cursoevaluacion.setCursoevaluacionpreguntaList(evaluacionPregunta);
			double totalDuracionCurso = 0;

			for (Cursoevaluacionpregunta cursoevaluacionpregunta : evaluacionPregunta) {

				double duracion = cursoevaluacionpregunta
						.getTiempodesarrolloList().get(0)
						.getSegundosTiempoPregunta() / 1000;
				preguntas.add(new PreguntaView(cursoevaluacionpregunta
						.getCodigoPregunta().getCodigo(),
						cursoevaluacionpregunta.getCodigoPregunta().getTexto(),
						duracion, cursoevaluacionpregunta
								.getCodigoCursoEvaluacion()
								.getCodigoCursoNivel().getCodigoSubtema()
								.getCodigoTema().getCodigoCurso().getNombre(),
						cursoevaluacionpregunta.getCodigoCursoEvaluacion()
								.getCodigoCursoNivel().getCodigoSubtema()
								.getCodigoTema().getNombre(),
						cursoevaluacionpregunta.getCodigoCursoEvaluacion()
								.getCodigoCursoNivel().getCodigoSubtema()
								.getNombre()));
				totalDuracionCurso += duracion;

				Integer lastInterval = (cursoevaluacionpregunta
						.getCodigoPregunta().getMinutosMaximo() > cursoevaluacionpregunta
						.getTiempodesarrolloList().get(0)
						.getSegundosTiempoPregunta()) ? cursoevaluacionpregunta
						.getCodigoPregunta().getMinutosMaximo()
						+ cursoevaluacionpregunta.getCodigoPregunta()
								.getMinutosMinimo() : cursoevaluacionpregunta
						.getTiempodesarrolloList().get(0)
						.getSegundosTiempoPregunta();

				List<Number> intervals = new ArrayList<Number>() {
					{
						add(cursoevaluacionpregunta.getCodigoPregunta()
								.getMinutosMinimo());
						add(cursoevaluacionpregunta.getCodigoPregunta()
								.getMinutosMaximo());
						add(lastInterval);
					}
				};

				MeterGaugeChartModel model = new MeterGaugeChartModel(
						cursoevaluacionpregunta.getTiempodesarrolloList()
								.get(0).getSegundosTiempoPregunta(), intervals);

				model.setTitle("Rangos:");
				model.setSeriesColors("cc6666,66cc66,cc6666");
				model.setGaugeLabel("Segundos");
				model.setShowTickLabels(true);
				model.setLabelHeightAdjust(110);
				model.setIntervalOuterRadius(100);

				rangosTiempos
						.add(new RangosTiempoPreguntasView(cursoevaluacion
								.getCodigoCursoNivel().getCodigoSubtema()
								.getCodigoTema().getCodigoCurso().getNombre(),
								cursoevaluacion.getCodigoCursoNivel()
										.getCodigoSubtema().getCodigoTema()
										.getNombre(), cursoevaluacion
										.getCodigoCursoNivel()
										.getCodigoSubtema().getNombre(),
								cursoevaluacionpregunta.getCodigoPregunta()
										.getTexto(), model));

			}

			distribucionSubtemas.add(new DistribucionTiempoView(cursoevaluacion
					.getCodigoCursoNivel().getCodigoSubtema().getCodigoTema()
					.getCodigoCurso().getNombre(), cursoevaluacion
					.getCodigoCursoNivel().getCodigoSubtema().getCodigoTema()
					.getNombre(), cursoevaluacion.getCodigoCursoNivel()
					.getCodigoSubtema().getNombre(), totalDuracionCurso));

			totalDuracion = +totalDuracionCurso;
		}

		distribucionDeCursos = new PieChartModel();
		distribucionDeCursos
				.setTitle("Distribucion de tiempo de Sub temas de Evaluacion (%)");
		distribucionDeCursos.setLegendPosition("w");

		for (DistribucionTiempoView distribucionTiempoView : distribucionSubtemas) {
			distribucionTiempoView.setPorcentaje((distribucionTiempoView
					.getPorcentaje() * 100) / totalDuracion);
			distribucionDeCursos.set(distribucionTiempoView.getCurso() + ", "
					+ distribucionTiempoView.getTema() + ", "
					+ distribucionTiempoView.getSubtema(),
					distribucionTiempoView.getPorcentaje());
		}

		FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.redirect(
						"/sistema-capacitaciones-tumi/app/capacitacion/analisisTiempo.xhtml");
	}

	public PersonalCapacitacion getPersonalCapacitacionSelected() {
		return personalCapacitacionSelected;
	}

	public void setPersonalCapacitacionSelected(
			PersonalCapacitacion personalCapacitacionSelected) {
		this.personalCapacitacionSelected = personalCapacitacionSelected;
	}

	public List<PersonalCapacitacion> getPersonalCapacitaciones() {
		return personalCapacitaciones;
	}

	public void setPersonalCapacitaciones(
			List<PersonalCapacitacion> personalCapacitaciones) {
		this.personalCapacitaciones = personalCapacitaciones;
	}

	public TreeNode getCapacitacionTree() {
		return capacitacionTree;
	}

	public void setCapacitacionTree(TreeNode capacitacionTree) {
		this.capacitacionTree = capacitacionTree;
	}

	public TreeNode getCursoSeleccionado() {
		return cursoSeleccionado;
	}

	public void setCursoSeleccionado(TreeNode cursoSeleccionado) {
		this.cursoSeleccionado = cursoSeleccionado;
	}

	public Cursonivel getCursonivel() {
		return cursonivel;
	}

	public void setCursonivel(Cursonivel cursonivel) {
		this.cursonivel = cursonivel;
	}

	public StreamedContent getStreamedContent() {
		if (streamedContent != null)
			try {
				streamedContent.getStream().reset();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // reset stream to the start position!
		return streamedContent;
	}

	public void setStreamedContent(StreamedContent streamedContent) {
		this.streamedContent = streamedContent;
	}

	public List<Cursocapacitacion> getCursosCapacitacion() {
		return cursosCapacitacion;
	}

	public void setCursosCapacitacion(List<Cursocapacitacion> cursosCapacitacion) {
		this.cursosCapacitacion = cursosCapacitacion;
	}

	public List<Evaluacion> getEvaluacionesResueltas() {
		return evaluacionesResueltas;
	}

	public void setEvaluacionesResueltas(List<Evaluacion> evaluacionesResueltas) {
		this.evaluacionesResueltas = evaluacionesResueltas;
	}

	public Evaluacion getSelectedEvaluacionResuelta() {
		return selectedEvaluacionResuelta;
	}

	public void setSelectedEvaluacionResuelta(
			Evaluacion selectedEvaluacionResuelta) {
		this.selectedEvaluacionResuelta = selectedEvaluacionResuelta;
	}

	public List<PreguntaView> getPreguntas() {
		return preguntas;
	}

	public void setPreguntas(List<PreguntaView> preguntas) {
		this.preguntas = preguntas;
	}

	public List<DistribucionTiempoView> getDistribucionSubtemas() {
		return distribucionSubtemas;
	}

	public void setDistribucionSubtemas(
			List<DistribucionTiempoView> distribucionSubtemas) {
		this.distribucionSubtemas = distribucionSubtemas;
	}

	public List<RangosTiempoPreguntasView> getRangosTiempos() {
		return rangosTiempos;
	}

	public void setRangosTiempos(List<RangosTiempoPreguntasView> rangosTiempos) {
		this.rangosTiempos = rangosTiempos;
	}

	public PieChartModel getDistribucionDeCursos() {
		return distribucionDeCursos;
	}

	public void setDistribucionDeCursos(PieChartModel distribucionDeCursos) {
		this.distribucionDeCursos = distribucionDeCursos;
	}

	public List<OrdenPreguntaView> getOrdenPreguntas() {
		return ordenPreguntas;
	}

	public void setOrdenPreguntas(List<OrdenPreguntaView> ordenPreguntas) {
		this.ordenPreguntas = ordenPreguntas;
	}

	public List<IntentoPreguntaView> getIntentoPreguntas() {
		return intentoPreguntas;
	}

	public void setIntentoPreguntas(List<IntentoPreguntaView> intentoPreguntas) {
		this.intentoPreguntas = intentoPreguntas;
	}

	public List<DistribucionTiempoView> getDistribucionSubtemasAprobados() {
		return distribucionSubtemasAprobados;
	}

	public void setDistribucionSubtemasAprobados(
			List<DistribucionTiempoView> distribucionSubtemasAprobados) {
		this.distribucionSubtemasAprobados = distribucionSubtemasAprobados;
	}

	public List<DistribucionTiempoView> getDistribucionSubtemasDesaprobados() {
		return distribucionSubtemasDesaprobados;
	}

	public void setDistribucionSubtemasDesaprobados(
			List<DistribucionTiempoView> distribucionSubtemasDesaprobados) {
		this.distribucionSubtemasDesaprobados = distribucionSubtemasDesaprobados;
	}

}
