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

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

import com.sv.tumi.db.dao.CursoCapacitacionDAO;
import com.sv.tumi.db.dao.CursoNivelDAO;
import com.sv.tumi.db.dao.EstadoDAO;
import com.sv.tumi.db.dao.PersonalCapacitacionDAO;
import com.sv.tumi.db.entity.Curso;
import com.sv.tumi.db.entity.Cursocapacitacion;
import com.sv.tumi.db.entity.Cursonivel;
import com.sv.tumi.db.entity.Estado;
import com.sv.tumi.db.entity.PersonalCapacitacion;
import com.sv.tumi.db.entity.Subtema;
import com.sv.tumi.db.entity.Tema;
import com.sv.tumi.view.CursoTreeView;
import com.sv.tumi.view.CursoView;
import com.sv.tumi.view.SubtemaView;
import com.sv.tumi.view.TemaView;

@ManagedBean(name = "capacitacionBean", eager = true)
@SessionScoped
public class CapacitacionBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CursoCapacitacionDAO cursoCapacitacionDAO = new CursoCapacitacionDAO();
	private CursoNivelDAO cursoNivelDAO = new CursoNivelDAO();
	private EstadoDAO estadoDAO = new EstadoDAO();
	private PersonalCapacitacionDAO personalCapacitacionDAO = new PersonalCapacitacionDAO();
	List<PersonalCapacitacion> personalCapacitaciones;
	PersonalCapacitacion personalCapacitacionSelected;
	private TreeNode capacitacionTree;
	private TreeNode cursoSeleccionado;
    Map<String, Object> filter = new HashMap<String, Object>();
    private Cursonivel cursonivel;
	private StreamedContent streamedContent;
	private List<Cursocapacitacion> cursosCapacitacion;
    
    ////////// FOR UPLOAD TEST*//////////////////
    
    private UploadedFile file;
    private Integer codigoCurso;
    
    public void upload(){
    	if(file != null) {
    		Cursonivel curso = cursoNivelDAO.find(codigoCurso);
    		curso.setContenido(file.getContents());
    		curso.setFormatoContenido("pdf");
    		cursoNivelDAO.edit(curso);
    		
            FacesMessage message = new FacesMessage("Succesful" + file.getFileName() + " is uploaded.");
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
    
    
    
    /////////////////////////////////////////////
	
    @PostConstruct
    public void init() {
    	capacitacionTree = new DefaultTreeNode();
    }
    
	public void goVerCapacitaciones() throws IOException {

		filter.clear();
		filter.put("codigoEstado.codigo", 5);//capacitacion generado
		personalCapacitaciones = personalCapacitacionDAO.findByProperty(filter);
				
		FacesContext.getCurrentInstance().getExternalContext()
				.redirect("/sistema-capacitaciones-tumi/app/capacitacion/consultar.xhtml");
	}
	
	public void goVerCursos() throws IOException {
				
		filter.clear();
		filter.put("codigoPersonalCapacitacion.codigo", personalCapacitacionSelected.getCodigo());
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
				
		FacesContext.getCurrentInstance().getExternalContext()
				.redirect("/sistema-capacitaciones-tumi/app/capacitacion/cursos.xhtml");
	}
	
	public void goVerContenido() throws IOException {
		
		if(cursoSeleccionado==null){
			return;
		}
		
		CursoTreeView curso = (CursoTreeView) cursoSeleccionado.getData();
		
		if(curso.isEnable()){			
			
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
			
			boolean vistoTodos=true;
			
			for (Cursocapacitacion cursoCapacitacion : cursosCapacitacion) {
				if(!cursoCapacitacion.isVisto()){
					vistoTodos=false;
					break;
				}
					
			}
			
			if(vistoTodos){
				Estado visto = estadoDAO.find(8);
				personalCapacitacionSelected.setCodigoEstado(visto);
				personalCapacitacionDAO.edit(personalCapacitacionSelected);
			}
			
			streamedContent = new DefaultStreamedContent(getData(cursonivel.getContenido()), "application/pdf");
			
			FacesContext.getCurrentInstance().getExternalContext()
			.redirect("/sistema-capacitaciones-tumi/app/capacitacion/contenido.xhtml");
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
	
	private void getCursosCapacitacion(List<CursoView> cursos, List<Cursocapacitacion> cursoCapacitacionList) {
		for (Cursocapacitacion cursoevaluacion : cursoCapacitacionList) {

			Curso curso = cursoevaluacion.getCodigoCursoNivel()
					.getCodigoSubtema().getCodigoTema().getCodigoCurso();
			Tema tema = cursoevaluacion.getCodigoCursoNivel()
					.getCodigoSubtema().getCodigoTema();
			Cursonivel subtema = cursoevaluacion.getCodigoCursoNivel();
			
			getCurso(cursos, curso, tema, subtema);

		}

	}
	
	private void getCurso(List<CursoView> cursos, Curso curso, Tema tema, Cursonivel cursoNivel) {
		CursoView cursoView = null;
		TemaView temaView = null;
		SubtemaView subtemaView = null;
		
		//CURSO
		
		for (CursoView cursoViewItem : cursos) {
			
			if(curso.getNombre().equalsIgnoreCase(cursoViewItem.getNombre())){
				cursoView = cursoViewItem;
			}
			
		}
		
		if(cursoView == null){
			cursoView = new CursoView();
			cursoView.setNombre(curso.getNombre());
			cursoView.setAlcance(curso.getAlcance());
			cursos.add(cursoView);
		}
		
		//TEMA
		
		for (TemaView temaViewItem : cursoView.getTema()) {
			
			if(tema.getNombre().equalsIgnoreCase(temaViewItem.getNombre())){
				temaView = temaViewItem;
			}
			
		}
		
		if(temaView == null){
			temaView = new TemaView();
			temaView.setNombre(tema.getNombre());
			temaView.setAlcance(tema.getAlcance());
			temaView.setOrden(tema.getOrden());
			cursoView.getTema().add(temaView);
		}
		
		//SUB TEMA
		
		for (SubtemaView subtemaViewItem : temaView.getSubtemas()) {
			
			if(cursoNivel.getCodigoSubtema().getNombre().equalsIgnoreCase(subtemaViewItem.getNombre())){
				subtemaView = subtemaViewItem;
			}
			
		}
		
		if(subtemaView == null){
			subtemaView = new SubtemaView();
			subtemaView.setNombre(cursoNivel.getCodigoSubtema().getNombre());
			subtemaView.setOrder(cursoNivel.getCodigoSubtema().getOrden());
			subtemaView.setAlcance(cursoNivel.getAlcance());
			subtemaView.setCodigoNivel(cursoNivel.getCodigoNivel().getCodigo());
			subtemaView.setCodigoSubtema(cursoNivel.getCodigoSubtema().getCodigo());
			temaView.getSubtemas().add(subtemaView);
		}
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
			} //reset stream to the start position!
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

}
