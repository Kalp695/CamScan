package search;

import java.io.IOException;
import java.util.List;
import core.Document;

/*******************************************************************
 * Searcher
 *
 * Provides the public interface for the search module.
 * 
 * @author dstorch
 * 
 *******************************************************************/

public interface Searcher {
	
	public SearchResults getSearchResults(String query, Document workingDocument, List<Document> allDocuments);
	
	public List<Term> sanitize(String text);
	
	public static class Factory {
		public static Searcher create() throws IOException {
			return new SearchManager();
		}
	}
}
