package humcare.zk.custom;

import humcare.dao.GenericDAO;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zul.AbstractListModel;
import org.zkoss.zul.FieldComparator;
import org.zkoss.zul.ListModelExt;
import org.zkoss.zul.event.ListDataEvent;

/**
 * Displaying and Sorting huge data using ZK.
 * http://books.zkoss.org/zk-mvvm-book/8.0/advanced/displaying_huge_amount_of_data.html
 * https://www.zkoss.org/wiki/Small_Talks/2011/March/Sorting_huge_data_using_ZK
 *
 * Esta classe é uma substituição ao SimpleListModel para ser utilizado em Grid
 * ou Listbox. Desse modo, o próprio HugeDataListModel irá conectar no banco
 * para realizar a busca do dados de forma paginada por meio do GenericDAO.
 *
 * @author alison
 */
public class HugeDataListModel extends AbstractListModel<Object> implements ListModelExt<Object> {

    private GenericDAO genericDAO;
    private String filtro = "";
    private String ordem = "";

    private Integer size;
    private boolean sortDirection;
    private Comparator<Object> sorting;

    private String CACHE_KEY;

    public HugeDataListModel(GenericDAO dao) {
        this.genericDAO = dao;
        this.CACHE_KEY = HugeDataListModel.class.getName() + "_" + dao.getClasse().getName() + "_cache";
    }

    @Override
    public Object getElementAt(int index) {
        Map<Integer, Object> cache = getCache();

        Object targetObject = cache.get(index);
        if (targetObject == null) {
            //if cache doesn't contain target object, query a page size of data starting from the index
            List<Object> pageResult = genericDAO.listarPaginado(filtro, ordem, index / this.getPageSize() + 1, this.getPageSize());
            int indexKey = index;
            for (Object o : pageResult) {
                cache.put(indexKey, o);
                indexKey++;
            }
        } else {
            return targetObject;
        }

        //get the target after query from database
        targetObject = cache.get(index);
        if (targetObject == null) {
            //if we still cannot find the target object, there is inconsistency between memory and the database
            throw new RuntimeException("Element at index " + index + " cannot be found in the database.");
        } else {
            return targetObject;
        }
    }

    @Override
    public int getSize() {
        if (size == null) {
            //the size changes when 'filtro' changes
            size = genericDAO.contar(filtro);
        }

        return size;
    }

    private Map<Integer, Object> getCache() {
        Execution execution = Executions.getCurrent();
        //we only reuse this cache in one execution to avoid accessing detached objects
        //so we will always have a cleared cache at each execution, for better performance and less memory usage
        //our filter opens a session during a HTTP request (every page change is a new request)
        Map<Integer, Object> cache = (Map) execution.getAttribute(CACHE_KEY);
        if (cache == null) {
            cache = new HashMap<>();
            execution.setAttribute(CACHE_KEY, cache);
        }
        return cache;
    }

    @Override
    public void sort(Comparator comparator, boolean ascending) {
        if (comparator instanceof FieldComparator) {
            //we cast the comparator to a FieldComparator which we use for the sorting declaration in ZUML, example (sort="auto(fieldName)")
            //in this case, the fieldName should be the same as the field name created in the database
            sorting = comparator;
            sortDirection = ascending;
            getCache().clear();
            ordem = " order by " + ((FieldComparator) comparator).getRawOrderBy() + (sortDirection ? " asc " : " desc ");
            fireEvent(ListDataEvent.STRUCTURE_CHANGED, -1, -1);
        }
    }

    @Override
    public void sort() {
        if (sorting == null) {
            throw new UiException("The sorting comparator is not assigned, please use sort(Comparator cmpr, final boolean ascending)");
        }
        sort(sorting, sortDirection);
    }

    @Override
    public String getSortDirection(Comparator cmpr) {
        if (Objects.equals(sortDirection, cmpr)) {
            return sortDirection ? "ascending" : "descending";
        }
        return "natural";
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        size = null;
        this.filtro = filtro;
    }

    public String getOrdem() {
        return ordem;
    }

    public void setOrdem(String ordem) {
        this.ordem = ordem;
    }

}
