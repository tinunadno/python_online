<template>
  <div>
    <div ref="editorContainer" class="editor"></div>
  </div>
</template>

<script>
import CodeMirror from 'codemirror';
import 'codemirror/lib/codemirror.css';

// Темы
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

    // Обработчик изменения содержимого редактора
    this.editor.on('change', (editor, change) => {
      const newValue = editor.getValue();
      this.$emit('input', newValue);

      // Выводим каждый новый символ в консоль
      if (change.origin === '+input') {
        const cursor = editor.getCursor(); // Получаем текущую позицию курсора
        const newChar = change.text[0]; // Новый введённый символ
        console.log(`Введён символ: "${newChar}" на позиции: строка ${cursor.line + 1}, столбец ${cursor.ch}`);

      }
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
  height: 100%;
  border: 1px solid #ddd;
}
</style>