package com.uncc.mobileappdev.inclass06;

import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Stephen on 2/25/2018.
 */

public class NewsParser {
    public static class NewsPullParser {
        static public ArrayList<Articles> parseArticles(InputStream inputStream) throws IOException, SAXException, XmlPullParserException {
            ArrayList<Articles> articleList = new ArrayList<>();
            Articles article = null;
            int counter = 0;

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, "UTF-8");

            int event = parser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT && counter <= 20) {

                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("title")) {
                            article = new Articles();
                            article.setTitle(parser.nextText().trim());
                            counter++;
                        } else if (parser.getName().equals("description")) {
                            String descInfo = parser.nextText().trim();
                            StringBuilder sb = new StringBuilder();
                            char[] chars = descInfo.toCharArray();
                            for(char c : chars){
                                if(c == '&' || c =='<'){
                                    break;
                                }
                                sb.append(c);
                            }
                            article.setDescription(sb.toString());

                        } else if (parser.getName().equals("pubDate")) {
                            article.setPublishedAtDate(parser.nextText().trim());
                        } else if (parser.getName().equals("media:content") && article.getUrlToImage() == null) {
                            article.setUrlToImage(parser.getAttributeValue(null, "url"));
                        } else if (parser.getName().equals("link")){
                            article.setUrl(parser.nextText().trim());
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item")){
                            articleList.add(article);
                        }
                        break;

                    default:
                        break;
                }

                event = parser.next();
            }


            return articleList;
        }
    }
}