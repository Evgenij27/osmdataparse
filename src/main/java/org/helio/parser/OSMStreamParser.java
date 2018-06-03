package org.helio.parser;

import org.helio.document.Bounds;
import org.helio.document.OSMDocument;
import org.helio.document.node.Node;
import org.helio.document.node.NodeBuilder;
import org.helio.document.relation.Member;
import org.helio.document.relation.MemberBuilder;
import org.helio.document.relation.Relation;
import org.helio.document.relation.RelationBuilder;
import org.helio.document.way.Way;
import org.helio.document.way.WayBuilder;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.math.BigDecimal;
import java.util.*;

public class OSMStreamParser {

    private final XMLStreamReader reader;

    private final List<Node> nodes = new ArrayList<>();
    private final List<Way> ways = new ArrayList<>();
    private final List<Relation> relations = new ArrayList<>();


    private Map<String, String> commonTags = new HashMap<>();
    private List<Member> commonMembers = new ArrayList<>();

    private NodeBuilder nodeBuilder = new NodeBuilder();
    private WayBuilder wayBuilder = new WayBuilder();
    private MemberBuilder memberBuilder = new MemberBuilder();
    private RelationBuilder relationBuilder = new RelationBuilder();
    private Bounds boundary;


    public OSMStreamParser(XMLStreamReader reader) {
        this.reader = reader;
    }

    public OSMDocument parseOSM() throws Exception {
        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    processStartElement();
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    processEndElement();
                    break;
            }
        }
        return new OSMDocument(this.boundary, nodes, ways, relations);
    }

    private void processStartElement() {
        String elementName = reader.getName().toString();
        switch (elementName) {
            case OSMDocument.NODE:
                processNodeStart();
                break;
            case OSMDocument.TAG:
                processTagStart();
                break;
            case OSMDocument.WAY:
                processWayStart();
                break;
            case OSMDocument.ND:
                processNdStart();
                break;
            case OSMDocument.RELATION:
                processRelationStart();
                break;
            case OSMDocument.MEMBER:
                processMemberStart();
                break;
            case OSMDocument.BOUNDS:
                System.out.println("Bounds ");
                processBoundaryStart();
                break;

        }
    }

    private void processEndElement() {
        String elementName = reader.getName().toString();
        switch (elementName) {
            case OSMDocument.NODE:
                processNodeEnd();
                break;
            case OSMDocument.WAY:
                processWayEnd();
                break;
            case OSMDocument.RELATION:
                processRelationEnd();
                break;
        }
    }

    private void processNodeStart() {
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            String attributeName = reader.getAttributeName(i).toString();
            String attributeValue = reader.getAttributeValue(i);
            switch (attributeName) {
                case Node.ID:
                    long id = Long.parseLong(attributeValue);
                    nodeBuilder.setId(id);
                    break;
                case Node.VISIBLE:
                    nodeBuilder.setVisible(Boolean.parseBoolean(attributeValue));
                    break;
                case Node.VERSION:
                    nodeBuilder.setVersion(Integer.parseInt(attributeValue));
                    break;
                case Node.CHANGESET:
                    nodeBuilder.setChangeset(Long.parseLong(attributeValue));
                    break;
                case Node.TIMESTAMP:
                    nodeBuilder.setTimestamp(attributeValue);
                    break;
                case Node.USER:
                    nodeBuilder.setUser(attributeValue);
                    break;
                case Node.UID:
                    nodeBuilder.setUid(Long.parseLong(attributeValue));
                    break;
                case Node.LATITUDE:
                    nodeBuilder.setLatitude(BigDecimal.valueOf(Double.parseDouble(attributeValue)));
                    break;
                case Node.LONGTITUDE:
                    nodeBuilder.setLongtitude(BigDecimal.valueOf(Double.parseDouble(attributeValue)));
                    break;
            }
        }
    }

    private void processNodeEnd() {
        for (Map.Entry<String, String> entry : commonTags.entrySet()) {
            nodeBuilder.addTag(entry.getKey(), entry.getValue());
        }
        Node node = nodeBuilder.createNode();
        nodes.add(node);
        nodeBuilder = new NodeBuilder();
        commonTags.clear();
    }

    private void processTagStart() {
        commonTags.put(reader.getAttributeValue(0), reader.getAttributeValue(1));

    }

    private void processWayStart() {
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            String attributeName = reader.getAttributeName(i).toString();
            String attributeValue = reader.getAttributeValue(i);
            switch (attributeName) {
                case Way.ID:
                    long id = Long.parseLong(attributeValue);
                    wayBuilder.setId(id);
                    break;
                case Way.VISIBLE:
                    wayBuilder.setVisible(Boolean.parseBoolean(attributeValue));
                    break;
                case Way.VERSION:
                    wayBuilder.setVersion(Integer.parseInt(attributeValue));
                    break;
                case Way.CHANGESET:
                    wayBuilder.setChangeset(Long.parseLong(attributeValue));
                    break;
                case Way.TIMESTAMP:
                    wayBuilder.setTimestamp(attributeValue);
                    break;
                case Way.USER:
                    wayBuilder.setUser(attributeValue);
                    break;
                case Way.UID:
                    wayBuilder.setUid(Long.parseLong(attributeValue));
                    break;
            }
        }
    }

    private void processNdStart() {
        Node node = null;
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            String attributeName = reader.getAttributeName(i).toString();
            String attributeValue = reader.getAttributeValue(i);
            switch (attributeName) {
                case "ref":
                    long ref = Long.parseLong(attributeValue);
                    Optional<Node> oNode = nodes.stream().filter(n -> ref == n.getId()).findFirst();
                    if (oNode.isPresent()) {
                        node = oNode.get();
                    }
                    wayBuilder.addNode(node);

                    break;
            }
        }
    }

    private void processWayEnd() {
        for (Map.Entry<String, String> entry : commonTags.entrySet()) {
            wayBuilder.addTag(entry.getKey(), entry.getValue());
        }
        Way way = wayBuilder.createWay();
        wayBuilder = new WayBuilder();
        commonTags.clear();
        ways.add(way);
    }

    private void processRelationStart() {
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            String attributeName = reader.getAttributeName(i).toString();
            String attributeValue = reader.getAttributeValue(i);
            switch (attributeName) {
                case Relation.ID:
                    long id = Long.parseLong(attributeValue);
                    relationBuilder.setId(id);
                    break;
                case Relation.VISIBLE:
                    relationBuilder.setVisible(Boolean.parseBoolean(attributeValue));
                    break;
                case Relation.VERSION:
                    relationBuilder.setVersion(Integer.parseInt(attributeValue));
                    break;
                case Relation.CHANGESET:
                    relationBuilder.setChangeset(Long.parseLong(attributeValue));
                    break;
                case Relation.TIMESTAMP:
                    relationBuilder.setTimestamp(attributeValue);
                    break;
                case Relation.USER:
                    relationBuilder.setUser(attributeValue);
                    break;
                case Relation.UID:
                    relationBuilder.setUid(Long.parseLong(attributeValue));
                    break;
            }
        }
    }

    private void processRelationEnd() {
        for (Map.Entry<String, String> entry : commonTags.entrySet()) {
            relationBuilder.addTag(entry.getKey(), entry.getValue());
        }
        commonTags.clear();

        for (Member member : commonMembers) {
            relationBuilder.addMember(member);
        }
        commonMembers.clear();

        Relation relation = relationBuilder.createRelation();
        relations.add(relation);
        relationBuilder = new RelationBuilder();
    }

    private void processMemberStart() {
        long ref = 0;
        String role = null;
        String type = null;
        Way way = null;
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            String attributeName = reader.getAttributeName(i).toString();
            String attributeValue = reader.getAttributeValue(i);
            switch (attributeName) {
                case Member.REF:
                    ref = Long.parseLong(attributeValue);
                    long id = ref;
                    Optional<Way> oWay = ways.stream().filter(w -> w.getId() == id).findFirst();
                    if (oWay.isPresent()) {
                        way = oWay.get();
                        ways.removeIf(w -> w.getId() == id);
                    }
                    break;
                case Member.ROLE:
                    role = attributeValue;
                    break;
                case Member.TYPE:
                    type = attributeValue;
                    break;
            }
        }
        if (way != null) {
            memberBuilder.setWay(way);
            memberBuilder.setRef(ref);
            memberBuilder.setRole(role);
            memberBuilder.setType(type);
            commonMembers.add(memberBuilder.createMember());
        }
    }

    private void processBoundaryStart() {
        BigDecimal minlat = null;
        BigDecimal maxlat = null;
        BigDecimal minlon = null;
        BigDecimal maxlon = null;
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            String attributeName = reader.getAttributeName(i).toString();
            String attributeValue = reader.getAttributeValue(i);
            switch (attributeName) {
                case Bounds.MINLAT:
                    minlat = BigDecimal.valueOf(Double.parseDouble(attributeValue));
                    break;
                case Bounds.MAXLAT:
                    maxlat = BigDecimal.valueOf(Double.parseDouble(attributeValue));
                    memberBuilder.setRole(attributeValue);
                    break;
                case Bounds.MINLON:
                    minlon = BigDecimal.valueOf(Double.parseDouble(attributeValue));
                    break;
                case Bounds.MAXLON:
                    maxlon = BigDecimal.valueOf(Double.parseDouble(attributeValue));
                    break;
            }
        }
        this.boundary = new Bounds(minlat, maxlat, minlon, maxlon);
    }
}