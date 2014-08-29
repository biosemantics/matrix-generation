package edu.arizona.biosemantics.matrixgeneration.model;

import edu.arizona.biosemantics.matrixgeneration.model.Taxon.Rank;

public class RankData implements Comparable<RankData> {
	private RankData parent;
	private String author;
	private Rank rank;
	private String name;
	private String date;

	public RankData(String author, String date, Rank rank, String name) {
		super();
		this.author = author;
		this.date = date;
		this.rank = rank;
		this.name = name;
	}
	
	public void setParent(RankData parent) {
		this.parent = parent;
	}
	
	public RankData getParent() {
		return parent;
	}

	public String getAuthor() {
		return author;
	}

	public Rank getRank() {
		return rank;
	}

	public String getName() {
		return name;
	}

	@Override
	public int compareTo(RankData o) {
		return rank.getId() - o.rank.getId();
	}

	public String getDate() {
		return date;
	}

	public String displayName() {
		return rank.toString() + "=" + name + "," + author + "," + date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + ((rank == null) ? 0 : rank.hashCode());
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
		RankData other = (RankData) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		if (rank != other.rank)
			return false;
		return true;
	}
	
	
}
