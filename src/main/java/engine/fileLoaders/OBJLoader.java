package engine.fileLoaders;

import engine.models.Model;
import engine.models.SimpleModel;
import maths.joml.Vector2f;
import maths.joml.Vector3f;
import maths.objects.Box;
import maths.objects.Triangle;
import maths.utils.Vector2;
import maths.utils.Vector3;
import opengl.constants.RenderMode;
import opengl.constants.VboUsage;
import opengl.objects.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class OBJLoader {

    private static final Map<String, Model> models = new HashMap<>();

    private static final String VERTEX = "v";
    private static final String TEXTURE = "vt";
    private static final String NORMAL = "vn";
    private static final String FACE = "f";

    private OBJLoader() {

    }

    public static Model loadOBJ(String fileName) throws Exception {
        Model m = models.get(fileName);
        if (m != null) {
            return m;
        }

        List<String> lines = TextFileLoader.readAllLines(fileName);

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        int v = 0, t = 0;
        for (String line : lines) {
            String[] tokens = line.split("\\s+");
            switch (tokens[0]) {
                case VERTEX:
                    Vector3f vec3f = Vector3.of(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                    vertices.add(vec3f);
                    v++;
                    break;
                case TEXTURE:
                    Vector2f vec2f = Vector2.of(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]));
                    textures.add(vec2f);
                    t++;
                    break;
                case NORMAL:
                    Vector3f vec3fNorm = Vector3.of(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                    normals.add(vec3fNorm);
                    break;
                case FACE:
                    addFace(faces, tokens);
                    break;
                default:
                    // Ignore other lines
                    break;
            }
        }

        m = reorderLists(vertices, textures, normals, faces);
        models.put(fileName, m);
        return m;
    }

    private static void addFace(List<Face> faces, String[] tokens) {
        switch (tokens.length) {
            case 4:
                // Triangle
                faces.add(new Face(tokens[1], tokens[2], tokens[3]));
                break;
            case 5:
                // Quad
                faces.add(new Face(tokens[1], tokens[2], tokens[3]));
                faces.add(new Face(tokens[1], tokens[3], tokens[4]));
                break;
            default:
                throw new IllegalArgumentException("Cannot process obj files with " +
                        "too complex faces, faces : " + tokens.length);
        }
    }

    private static Model reorderLists(List<Vector3f> posList, List<Vector2f> texList,
                                      List<Vector3f> normList, List<Face> facesList) {

        final Vector3f min = Vector3.of(Integer.MAX_VALUE);
        final Vector3f max = Vector3.of(Integer.MIN_VALUE);

        List<Integer> indices = new ArrayList<>();
        float[] positions = new float[posList.size() * 3];
        int pointer = 0;
        for (Vector3f pos : posList) {
            positions[pointer++] = pos.x;
            positions[pointer++] = pos.y;
            positions[pointer++] = pos.z;
            min.min(pos);
            max.max(pos);
        }
        float[] texCoords = new float[posList.size() * 2];
        float[] normalVec = new float[posList.size() * 3];

        for (Face face : facesList) {
            IdxGroup[] faceVertexIndices = face.getFaceVertexIndices();
            for (IdxGroup indValue : faceVertexIndices) {
                processFaceVertex(indValue, texList, normList,
                        indices, texCoords, normalVec);
            }
        }
        int[] indicesArr = indices.stream().mapToInt(v -> v).toArray();
        final IndexBuffer indexBuffer = BufferUtils.loadToIndexBuffer(VboUsage.STATIC_DRAW, indicesArr);

        float[] data = connectData(positions.length / 3, positions, texCoords, normalVec);
        final DataBuffer dataBuffer = BufferUtils.loadToDataBuffer(VboUsage.STATIC_DRAW, data);

        final Vao vao = Vao.create();
        vao.loadDataBuffer(dataBuffer, Attribute.ofPositions(),
                Attribute.ofTexCoords(), Attribute.ofNormals());

        return new SimpleModel(vao, RenderMode.TRIANGLES, Box.of(min, max), indexBuffer);
    }

    private static void processFaceVertex(IdxGroup indices, List<Vector2f> textCoordList,
                                          List<Vector3f> normList, List<Integer> indicesList,
                                          float[] texCoordArr, float[] normArr) {

        // Set index for vertex coordinates
        int posIndex = indices.idxPos;
        indicesList.add(posIndex);

        // Reorder texture coordinates
        if (indices.idxTextCoord >= 0) {
            Vector2f textCoord = textCoordList.get(indices.idxTextCoord);
            texCoordArr[posIndex * 2] = textCoord.x;
            texCoordArr[posIndex * 2 + 1] = 1 - textCoord.y;
        }
        // Reorder vector normals
        if (indices.idxVecNormal >= 0) {
            Vector3f vecNorm = normList.get(indices.idxVecNormal);
            normArr[posIndex * 3] = vecNorm.x;
            normArr[posIndex * 3 + 1] = vecNorm.y;
            normArr[posIndex * 3 + 2] = vecNorm.z;
        }
    }

    /**
     * Connect multiple data arrays into one array keeping the order needed for the vbo
     *
     * @param vertexCount the amount of vertices
     * @param data        array of data arrays
     * @return connected data array
     */
    private static float[] connectData(int vertexCount, float[]... data) {
        int totalLength = 0;
        int[] compCount = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            totalLength += data[i].length;
            compCount[i] = data[i].length / vertexCount;
        }
        float[] connectedData = new float[totalLength];
        int pointer = 0;
        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < data.length; j++) {
                for (int k = 0; k < compCount[j]; k++) {
                    connectedData[pointer++] = data[j][i * compCount[j] + k];
                }
            }
        }

        return connectedData;
    }

    private static List<Triangle> getTriangles(List<Vector3f> vertices, List<Face> faces) {
        List<Triangle> triangles = new ArrayList<>();
        for (Face face : faces) {
            Vector3f p1 = vertices.get(face.idxGroups[0].idxPos);
            Vector3f p2 = vertices.get(face.idxGroups[1].idxPos);
            Vector3f p3 = vertices.get(face.idxGroups[2].idxPos);
            Triangle triangle = new Triangle(p1, p2, p3);
            triangles.add(triangle);
        }
        return triangles;
    }

    private static class Face {

        /**
         * List of idxGroup groups for a face triangle (3 vertices per face).
         */
        private IdxGroup[] idxGroups;

        private Face(String v1, String v2, String v3) {
            idxGroups = new IdxGroup[3];
            // Parse the lines
            idxGroups[0] = parseLine(v1);
            idxGroups[1] = parseLine(v2);
            idxGroups[2] = parseLine(v3);
        }

        private IdxGroup parseLine(String line) {
            IdxGroup idxGroup = new IdxGroup();

            String[] lineTokens = line.split("/");
            int length = lineTokens.length;
            idxGroup.idxPos = Integer.parseInt(lineTokens[0]) - 1;
            if (length > 1) {
                // It can be empty if the obj does not define text coords
                String textCoord = lineTokens[1];
                idxGroup.idxTextCoord = textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : IdxGroup.NO_VALUE;
                if (length > 2) {
                    idxGroup.idxVecNormal = Integer.parseInt(lineTokens[2]) - 1;
                }
            }

            return idxGroup;
        }

        private IdxGroup[] getFaceVertexIndices() {
            return idxGroups;
        }
    }

    private static class IdxGroup {

        private static final int NO_VALUE = -1;

        private int idxPos;
        private int idxTextCoord;
        private int idxVecNormal;

        private IdxGroup() {
            idxPos = NO_VALUE;
            idxTextCoord = NO_VALUE;
            idxVecNormal = NO_VALUE;
        }
    }

}
