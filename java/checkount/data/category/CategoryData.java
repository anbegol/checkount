package checkount.data.category;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Category class
 *
 */
public class CategoryData implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** Category id */
	private String idCategory;
	/** Description */
	private String description;
	/** Regular expression */
	private String regularExp;
	/** Id category Father */
	private String idCategoryFather;
	/** List of regulation expressions */
	private Set<String> regularExpSet;
	
	/** Total by idCategory */
	private double total;
	/** List of direct children */
	private List<CategoryData> childrenList;

	public String getIdCategory() {
		return idCategory;
	}

	public void setIdCategory(String idCategory) {
		this.idCategory = idCategory;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getRegularExp() {
		return regularExp;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double number) {
		this.total += number;
	}

	public void setRegularExp(String regularExp) {
		this.regularExp = regularExp;
		
		if (regularExp != null) {
			if (regularExpSet == null) {
				regularExpSet = new HashSet<>();
			}
			
			String[] split = regularExp.split(";");
			for (String str : split) {
				regularExpSet.add(str);
			}
		}
	}
	
	public boolean contains(String description) {
		Boolean res = Boolean.FALSE;
		if(description != null && regularExpSet != null) {
			for (String str : regularExpSet) {
				Pattern pat = Pattern.compile(str);
				Matcher mat = pat.matcher(description);
				res = mat.matches();
				if(res) {
					break;
				}
			}
		}
		return res;
	}

	public String getIdCategoryFather() {
		return idCategoryFather;
	}

	public void setIdCategoryFather(String idCategoryFather) {
		this.idCategoryFather = idCategoryFather;
	}

	public Set<String> getRegularExpSet() {
		return regularExpSet;
	}

	public void setRegularExpSet(Set<String> regularExpSet) {
		this.regularExpSet = regularExpSet;
	}

	public List<CategoryData> getChildrenList() {
		return childrenList;
	}

	public void setChildrenList(List<CategoryData> childrenList) {
		this.childrenList = childrenList;
	}

	@Override
	public String toString() {
		return "CategoryData [idCategory=" + idCategory + ", description=" + description + ", idCategoryFather=" + idCategoryFather
				+ ", total=" + total + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idCategory == null) ? 0 : idCategory.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategoryData other = (CategoryData) obj;
		if (idCategory == null) {
			if (other.idCategory != null)
				return false;
		} else if (!idCategory.equals(other.idCategory))
			return false;
		return true;
	}
}
