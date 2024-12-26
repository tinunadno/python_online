<template>
  <div>
    <div ref="editorContainer" class="editor"></div>
  </div>
</template>

<script>
import CodeMirror from 'codemirror';
import 'codemirror/lib/codemirror.css';

//темы
import 'codemirror/theme/dracula.css';
import 'codemirror/theme/monokai.css';

export default {
  name: 'CodeMirrorEditor',
  props: {
    value: {
      type: String,
      required: false,
      default: '',
    },
    options: {
      type: Object,
      default: () => ({
        mode: 'python',
        theme: 'dracula',
        lineNumbers: true,
        indentUnit: 4,
        tabSize: 4,
      }),
    },
  },
  mounted() {
    this.editor = CodeMirror(this.$refs.editorContainer, {
      value: this.value,
      ...this.options,
    });

    this.editor.on('change', () => {
      const newValue = this.editor.getValue();
      this.$emit('input', newValue);
    });
  },
  methods: {
    setTheme(theme) {
      this.editor.setOption('theme', theme);
    },
  },
  watch: {
    value(newValue) {
      if (newValue !== this.editor.getValue()) {
        this.editor.setValue(newValue);
      }
    },
  },
};
</script>

<style>
.editor {
  height: 400px;
  border: 1px solid #ddd;
}
</style>
