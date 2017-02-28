package checkount.data.movement;

import java.io.Serializable;
import java.util.Date;

/**
 * Movement class
 *
 */
public class MovementData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** Id Movement */
	private String idMovement;
	/** Date movements */
	private Date date;
	/** Description */
	private String descripction;
	/** Value */
	private Double import_m;
	/** Category */
	private String category;
	
	/**
	 * Constructor
	 * @param idMoviment
	 * @param date
	 * @param descripction
	 * @param import_m
	 */
	public MovementData(String idMoviment, Date date, String descripction, Double import_m) {
		super();
		this.idMovement = idMoviment;
		this.date = date;
		this.descripction = descripction;
		this.import_m = import_m;
	}

	public String getIdMovement() {
		return idMovement;
	}

	public void setIdMovement(String idMovement) {
		this.idMovement = idMovement;
	}

	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public String getDescripction() {
		return descripction;
	}


	public void setDescripction(String descripction) {
		this.descripction = descripction;
	}
	
	public Double getImport_m() {
		return import_m;
	}

	public void setImport_m(Double import_m) {
		this.import_m = import_m;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "MovementData [idMoviment=" + idMovement + ", date=" + date + ", descripction=" + descripction
				+ ", import_m=" + import_m + ", category=" + category + "]";
	}
}
