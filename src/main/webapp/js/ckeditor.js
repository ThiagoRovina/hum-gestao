CKEDITOR.editorConfig = function(config) {
    config.resize_enabled = false;
    config.language = 'pt-BR';
    config.toolbar = 'Custom';
    config.toolbar_Simple = [ [ 'Bold', 'Italic', '-', 'NumberedList', 'BulletedList', '-', 'Link', 'Unlink', '-', 'About' ] ];
    config.toolbar_Complex = [
            [ 'Bold', 'Italic', 'Underline', 'Strike', 'Subscript',
                    'Superscript', 'TextColor', 'BGColor', '-', 'Cut', 'Copy',
                    'Paste', 'Link', 'Unlink', 'Image'],
            [ 'Undo', 'Redo', '-', 'JustifyLeft', 'JustifyCenter',
                    'JustifyRight', 'JustifyBlock' ],
            [ 'Table', 'Smiley', 'SpecialChar', 'PageBreak',
                    'Styles', 'Format', 'Font', 'FontSize', 'Maximize'] ];
    config.toolbar_Custom = [
            [ 'Font', 'FontSize'] ,
            [ 'Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript', 
                '-', 'TextColor', 'BGColor', 
                '-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock'],
            ['Link', 'Unlink', 'Image'],
            [ 'Source', '-', 'Maximize', 'About']
    ];
};