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
          <xsl:for-each select="countries/element[area &lt;= 10000.0 and region = &quot;Europe&quot;]">
            <xsl:sort select="translations/fr"/>
            <div class="col-sm-3">
              <button class="btn btn-default" data-toggle="modal" type="button">
                <xsl:attribute name="data-target">
                  <xsl:value-of select="concat('#',alpha3Code)"/>
                </xsl:attribute>
                <xsl:value-of select="translations/fr"/>
                <img width="25.0">
                  <xsl:attribute name="src">
                    <xsl:value-of select="flag"/>
                  </xsl:attribute>
                </img>
              </button>
            </div>
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
                  <div class="modal-body">
                    <table>
                      <tr>
                        <td>
                          <img width="100.0">
                            <xsl:attribute name="src">
                              <xsl:value-of select="flag"/>
                            </xsl:attribute>
                          </img>
                        </td>
                        <td>
                          <xsl:value-of select="concat('Capitale : ',capital)"/>
                          <br/>
                          <xsl:value-of select="concat('Population : ',population, ' habitants')"/>
                          <br/>
                          <xsl:value-of select="concat('Superficie : ',area, ' km2')"/>
                          <br/>
                          <xsl:value-of select="concat('Continent : ',region)"/>
                          <br/>
                          <xsl:value-of select="concat('Sous-Continent : ',subregion)"/>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          <div class="panel panel-default">
                            <div class="panel panel-heading">Langues parlées</div>
                            <div class="panel panel-body">
                              <table class="table table-bordered">
                                <xsl:for-each select="languages/element">
                                  <tr>
                                    <td>
                                      <xsl:value-of select="name"/>
                                    </td>
                                  </tr>
                                </xsl:for-each>
                              </table>
                            </div>
                          </div>
                        </td>
                      </tr>
                    </table>
                  </div>
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
