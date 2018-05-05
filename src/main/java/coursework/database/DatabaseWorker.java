package coursework.database;

import coursework.database.entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class DatabaseWorker
{
    private static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    //-------------------------------------------------------------------
    // Get all
    //-------------------------------------------------------------------

    public static List<AdvertisementEntity> getAllAds()
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM AdvertisementEntity ");
        List<AdvertisementEntity> ads = (List<AdvertisementEntity>)query.list();
        session.close();
        return ads;
    }

    public static List<AdvertisementTagEntity> getAllAdTags()
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM AdvertisementTagEntity ");
        List<AdvertisementTagEntity> adTags = (List<AdvertisementTagEntity>)query.list();
        session.close();
        return adTags;
    }

    public static List<CategoryEntity> getAllCategories()
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM CategoryEntity ");
        List<CategoryEntity> categories = (List<CategoryEntity>)query.list();
        session.close();
        return categories;
    }

    public static List<TagEntity> getAllTags()
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM TagEntity ");
        List<TagEntity> tags = (List<TagEntity>)query.list();
        session.close();
        return tags;
    }

    public static List<UserEntity> getAllUsers()
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM UserEntity");
        List<UserEntity> users = (List<UserEntity>)query.list();
        session.close();
        return users;
    }

    //-------------------------------------------------------------------
    // Get by id
    //-------------------------------------------------------------------

    public static AdvertisementEntity getAd(int id)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        AdvertisementEntity ad = (AdvertisementEntity) session.get(AdvertisementEntity.class, id);
        session.close();
        return ad;
    }

    public static CategoryEntity getCategory(int id)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        CategoryEntity category = (CategoryEntity) session.get(CategoryEntity.class, id);
        session.close();
        return category;
    }

    public static TagEntity getTag(int id)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        TagEntity tag = (TagEntity) session.get(TagEntity.class, id);
        session.close();
        return tag;
    }

    public static UserEntity getUser(int id)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        UserEntity document = (UserEntity) session.get(UserEntity.class, id);
        session.close();
        return document;
    }

    //-------------------------------------------------------------------
    // Other getters
    //-------------------------------------------------------------------

    public static List<AdvertisementEntity> getAdsByHeadlineOrContent(String headlineParam, String contentParam)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM AdvertisementEntity WHERE LOWER(headline) LIKE " +
                likePattern(headlineParam) + " OR LOWER(content) LIKE " + likePattern(contentParam));
        List<AdvertisementEntity> ads = (List<AdvertisementEntity>)query.list();
        session.close();
        return ads;
    }

    public static List<AdvertisementEntity> getAdsByCategoryId(int categoryIdParam)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM AdvertisementEntity WHERE categoryId = " + quote(categoryIdParam));
        List<AdvertisementEntity> ads = (List<AdvertisementEntity>)query.list();
        session.close();
        return ads;
    }

    public static List<AdvertisementEntity> getAdsByUserId(int userIdParam)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM AdvertisementEntity WHERE userId = " + quote(userIdParam));
        List<AdvertisementEntity> ads = (List<AdvertisementEntity>)query.list();
        session.close();
        return ads;
    }

    public static List<AdvertisementEntity> getAdsByActuality(byte actualParam)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM AdvertisementEntity WHERE actual = " + quote(actualParam));
        List<AdvertisementEntity> ads = (List<AdvertisementEntity>)query.list();
        session.close();
        return ads;
    }

    public static List<AdvertisementEntity> getAdsFromDate(String dateFrom)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM AdvertisementEntity WHERE publishTime > " + quote(dateFrom));
        List<AdvertisementEntity> ads = (List<AdvertisementEntity>)query.list();
        session.close();
        return ads;
    }

    public static List<AdvertisementEntity> getAdsToDate(String dateTo)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM AdvertisementEntity WHERE publishTime < " + quote(dateTo));
        List<AdvertisementEntity> ads = (List<AdvertisementEntity>)query.list();
        session.close();
        return ads;
    }

    public static List<AdvertisementEntity> getAdsBetweenDates(String dateFrom, String dateTo)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM AdvertisementEntity WHERE publishTime < " + quote(dateTo) + " AND publishTime > " + quote(dateFrom));
        List<AdvertisementEntity> ads = (List<AdvertisementEntity>)query.list();
        session.close();
        return ads;
    }

    public static List<AdvertisementTagEntity> getAdTagsByAdId(int adIdParam)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM AdvertisementTagEntity WHERE advertisementId = " + quote(adIdParam));
        List<AdvertisementTagEntity> adTags = (List<AdvertisementTagEntity>)query.list();
        session.close();
        return adTags;
    }

    public static List<AdvertisementTagEntity> getAdTagsByTagId(int tagIdParam)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM AdvertisementTagEntity WHERE tagId = " + quote(tagIdParam));
        List<AdvertisementTagEntity> adTags = (List<AdvertisementTagEntity>)query.list();
        session.close();
        return adTags;
    }

    public static List<CategoryEntity> getCategoriesByName(String categoryName)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM CategoryEntity WHERE LOWER(name) = LOWER(" + quote(categoryName) + ")");
        List<CategoryEntity> categories = (List<CategoryEntity>)query.list();
        session.close();
        return categories;
    }

    public static List<CategoryEntity> getCategoriesByLikeName(String categoryName)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM CategoryEntity WHERE LOWER(name) LIKE " + likePattern(categoryName));
        List<CategoryEntity> categories = (List<CategoryEntity>)query.list();
        session.close();
        return categories;
    }

    public static List<TagEntity> getTagsByName(String tagName)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM TagEntity WHERE LOWER(name) = LOWER(" + quote(tagName) + ")");
        List<TagEntity> tags = (List<TagEntity>)query.list();
        session.close();
        return tags;
    }

    public static List<TagEntity> getTagsByLikeName(String tagName)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM TagEntity WHERE LOWER(name) LIKE " + likePattern(tagName));
        List<TagEntity> tags = (List<TagEntity>)query.list();
        session.close();
        return tags;
    }

    public static List<UserEntity> getUsersByLogin(String loginParam)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM UserEntity WHERE login = " + quote(loginParam));
        List<UserEntity> users = (List<UserEntity>)query.list();
        session.close();
        return users;
    }

    public static List<UserEntity> getUsersByRole(byte role)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM UserEntity WHERE type = " + quote(role));
        List<UserEntity> users = (List<UserEntity>)query.list();
        session.close();
        return users;
    }

    //-------------------------------------------------------------------
    // Add entity
    //-------------------------------------------------------------------

    public static int addAd(AdvertisementEntity ad)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        int id = (int) session.save(ad);
        session.getTransaction().commit();
        session.close();
        return id;
    }

    public static void addAdTag(AdvertisementTagEntity adTag)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(adTag);
        session.getTransaction().commit();
        session.close();
    }

    public static int addCategory(CategoryEntity category)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        int id = (int)session.save(category);
        session.getTransaction().commit();
        session.close();
        return id;
    }

    public static int addTag(TagEntity tag)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        int id = (Integer)session.save(tag);
        session.getTransaction().commit();
        session.close();
        return id;
    }

    public static int addUser(UserEntity user)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        int id = (int)session.save(user);
        session.getTransaction().commit();
        session.close();
        return id;
    }

    //-------------------------------------------------------------------
    // Delete entity
    //-------------------------------------------------------------------

    public static void deleteAd(AdvertisementEntity ad)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(ad);
        session.getTransaction().commit();
        session.close();
    }

    public static void deleteAdTag(AdvertisementTagEntity adTag)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(adTag);
        session.getTransaction().commit();
        session.close();
    }

    public static void deleteCategory(CategoryEntity category)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(category);
        session.getTransaction().commit();
        session.close();
    }

    public static void deleteTag(TagEntity tag)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(tag);
        session.getTransaction().commit();
        session.close();
    }

    public static void deleteUser(UserEntity user)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(user);
        session.getTransaction().commit();
        session.close();
    }

    //-------------------------------------------------------------------
    // Update entity
    //-------------------------------------------------------------------

    public static void updateAd(AdvertisementEntity ad)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(ad);
        session.getTransaction().commit();
        session.close();
    }

    public static void updateAdTag(AdvertisementTagEntity adTag)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(adTag);
        session.getTransaction().commit();
        session.close();
    }

    public static void updateCategory(CategoryEntity category)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(category);
        session.getTransaction().commit();
        session.close();
    }

    public static void updateTag(TagEntity tag)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(tag);
        session.getTransaction().commit();
        session.close();
    }

    public static void updateUser(UserEntity user)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(user);
        session.getTransaction().commit();
        session.close();
    }

    //------------------------------------------------------------------------

    private static String likePattern(String string) {
        return "LOWER('%" + string + "%')";
    }

    private static String quote(String string) {
        return "'" + string + "'";
    }

    private static String quote(Integer i) {
        return "'" + i + "'";
    }

    private static String quote(byte b) {
        return "'" + b + "'";
    }

    //-------------------------------------------------------------------
    //
    //-------------------------------------------------------------------

    public static List<String> getCategoryNames()
    {
        List<String> categoryNames = new ArrayList<>();
        List<CategoryEntity> categories = DatabaseWorker.getAllCategories();
        for (CategoryEntity category : categories)
            categoryNames.add(category.getName());
        return categoryNames;
    }

    public static List<String> getTagNames()
    {
        List<String> tagNames = new ArrayList<>();
        List<TagEntity> tags = DatabaseWorker.getAllTags();
        for (TagEntity tag : tags)
            tagNames.add(tag.getName());
        return tagNames;
    }

    public static boolean loginExists(String login)
    {
        List<UserEntity> users = DatabaseWorker.getUsersByLogin(login);
        return !users.isEmpty();
    }

    public static boolean categoryExists(String categoryName)
    {
        List<CategoryEntity> categories = DatabaseWorker.getCategoriesByName(categoryName);
        return !categories.isEmpty();
    }

    public static boolean tagExists(String tagName)
    {
        List<TagEntity> tags = DatabaseWorker.getTagsByName(tagName);
        return !tags.isEmpty();
    }
}
