package com.example.ecommerce.search;

import com.example.ecommerce.model.Product;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class LuceneIndexService {

    private Directory directory;
    private StandardAnalyzer analyzer;

    @PostConstruct
    public void init() throws IOException {
        analyzer = new StandardAnalyzer();
        Path indexPath = Path.of(System.getProperty("java.io.tmpdir"), "ecommerce-lucene-index");
        directory = FSDirectory.open(indexPath);
    }

    public void indexProduct(Product p) throws IOException {
        try (IndexWriter writer = createWriter()) {
            Document doc = new Document();
            doc.add(new StringField("id", String.valueOf(p.getId()), Field.Store.YES));
            doc.add(new TextField("name", p.getName() == null ? "" : p.getName(), Field.Store.YES));
            doc.add(new TextField("description", p.getDescription() == null ? "" : p.getDescription(), Field.Store.YES));
            doc.add(new DoublePoint("price", p.getPrice() == null ? 0.0 : p.getPrice()));
            doc.add(new StoredField("price_display", p.getPrice() == null ? 0.0 : p.getPrice()));
            writer.updateDocument(new Term("id", String.valueOf(p.getId())), doc);
            writer.commit();
        }
    }

    public List<Long> search(String q, int limit) throws Exception {
        try (DirectoryReader reader = DirectoryReader.open(directory)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser parser = new QueryParser("name", analyzer);
            Query query = parser.parse(QueryParser.escape(q));
            TopDocs results = searcher.search(query, limit);
            List<Long> ids = new ArrayList<>();
            for (ScoreDoc sd : results.scoreDocs) {
                Document d = searcher.doc(sd.doc);
                ids.add(Long.valueOf(d.get("id")));
            }
            return ids;
        }
    }

    private IndexWriter createWriter() throws IOException {
        IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
        return new IndexWriter(directory, cfg);
    }
}
