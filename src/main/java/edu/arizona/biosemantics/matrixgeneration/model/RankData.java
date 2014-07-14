package edu.arizona.biosemantics.matrixgeneration.model;

import edu.arizona.biosemantics.matrixgeneration.model.Taxon.Rank;

public class RankData implements Comparable<RankData> {
	private String author;
	private Rank rank;
	private String name;

	public RankData(String author, Rank rank, String name) {
		super();
		this.author = author;
		this.rank = rank;
		this.name = name;
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
	
	@Override
	public String toString() {
		return rank.name() + "=" + name + " by " + author;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (rank != other.rank)
			return false;
		return true;
	}
}
