package org.nymo.engine.scene;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.nymo.engine.renderer.Shader;

public class LevelEditorScene extends Scene {

    private String vertexShaderSrc = "#version 330 core\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";

    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexID, fragmentID, shaderProgram;

    private float[] vertexArray ={
        // Position             // Colour [R, G, B, A]
         0.5f, -0.5f, 0.0f,     1.0f, 0.0f, 0.0f, 1.0f, // Bottom right
        -0.5f,  0.5f, 0.0f,     0.0f, 1.0f, 0.0f, 1.0f, // Top Left
         0.5f,  0.5f, 0.0f,     0.0f, 0.0f, 1.0f, 1.0f, // Top Right
        -0.5f, -0.5f, 0.0f,     1.0f, 1.0f, 0.0f, 1.0f, // Bottom Left
    };

    // Counter clockwise order //
    private int[] elementArray = {
        2, 1, 0, // Top right triangle //
        0, 1, 3, // Bottom left trangle //
    };

    Shader defaultShader;

    private int vaoID, vboID, eboID;

    public LevelEditorScene() {
    }

    @Override
    public void init() {
        defaultShader = new Shader("engine/assets/shaders/default.glsl");
        defaultShader.compile();
        
          /////////////////////////////////////////////////////////////////////
         //// Generate VAO, VBO, and EBO buffers and send them to the GPU ////
        /////////////////////////////////////////////////////////////////////
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices ..
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer //
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload //
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers //
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        defaultShader.use();
        // Bind the VAO //
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers //
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything //
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();
    }
    
}
