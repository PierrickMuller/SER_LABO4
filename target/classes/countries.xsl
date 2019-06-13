<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output doctype-public="-//W3C//DTD HTML 4.01//EN" doctype-system="http://www.w3.org/TR/html4/strict.dtd" encoding="UTF-8" indent="yes" method="html"/>
  <xsl:template match="/">
    <html>
      <head>
        <title>Liste des pays</title>
        <script src="js/jquery-3.4.1.min.js"/>
        <script src="js/bootstrap.min.js"/>
        <link href="css/bootstrap.min.css" rel="stylesheet"/>
      </head>
      <body>
        <div class="row">
          <xsl:for-each select="countries/element">
            <xsl:sort select="translations/fr"/>
            <a data-toggle="modal">
              <xsl:attribute name="data-target">
                <xsl:value-of select="concat('#',alpha3Code)"/>
              </xsl:attribute>
              <div class="col-sm-3">
                <xsl:value-of select="translations/fr"/>
                <img width="25">
                  <xsl:attribute name="src">
                    <xsl:value-of select="flag"/>
                  </xsl:attribute>
                </img>
              </div>
            </a>
            <div aria-hidden="true" aria-labelledby="exampleModalLabel" class="modal fade" role="dialog" tabindex="-1">
              <xsl:attribute name="id">
                <xsl:value-of select="alpha3Code"/>
              </xsl:attribute>
              <div class="modal-dialog" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title">
                      <xsl:value-of select="translations/fr"/>
                    </h5>
                  </div>
                  <div class="modal-body"/>
                  <div class="modal-footer">
                    <button class="btn btn-primary" data-dismiss="modal" type="button">Fermer</button>
                  </div>
                </div>
              </div>
            </div>
          </xsl:for-each>
        </div>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
