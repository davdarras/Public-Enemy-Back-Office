<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">
    
    <xsl:output 
        method="xml"
        encoding="UTF-8"
        indent="yes" 
    /> 
    
    <xsl:template match="/">
     <xsl:variable name="arbre">
            <!--on code les balises <variables>-->            
            <xsl:element name="Variables">
                <!--apply templates permet le parcours des éléments et pas juste racine
                 et d'appliquer le template définit plus bas-->               
                <xsl:apply-templates select="/*"/>
            </xsl:element>        
        </xsl:variable>
        <!--on code les balises <variables>--> 
        <xsl:element name="Variables">
            <!--on a appliquer le template du bloc plus bas
            on prend les valeurs distinctes des champs qui nous intéressent et on les 
            transforme en balise via xsl element avec texte vide--> 
            <xsl:for-each select="distinct-values($arbre/Variables/*/name())">
                <xsl:element name="{.}">
                    <xsl:text> </xsl:text>
                </xsl:element>
            </xsl:for-each>
        </xsl:element>
   
        
    </xsl:template>
    
    <xsl:template match="text()">
        <xsl:variable name="regex" select="'(\$!|\$)\{[a-zA-Z_0-9]*\}'"/>
        <xsl:analyze-string select="." regex="{$regex}">
            <xsl:matching-substring>
                        <xsl:variable name="varz" select="tokenize(.,'\{')"/>
                        <xsl:for-each select="$varz[position()>1]">
                            <xsl:element name="{substring-before(.,'}')}"> 
                                <xsl:text> </xsl:text>
                            </xsl:element>                
                        </xsl:for-each>
          
            </xsl:matching-substring>
        </xsl:analyze-string>        
    </xsl:template>
    
</xsl:stylesheet>